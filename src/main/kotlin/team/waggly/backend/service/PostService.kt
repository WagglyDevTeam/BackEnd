package team.waggly.backend.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.dto.PagingResponseDto
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.post.*
import team.waggly.backend.model.*
import team.waggly.backend.repository.*
import team.waggly.backend.repository.querydsl.QPostRepository
import team.waggly.backend.service.awsS3.S3Uploader
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional

@Service
class PostService(
    private val postRepository: PostRepository,
    private val qPostRepository: QPostRepository,
    private val postLikeRepository: PostLikeRepository,
    private val commentRepository: CommentRepository,
    private val postImageRepository: PostImageRepository,
    private val commentLikeRepository: CommentLikeRepository,
    private val s3Uploader: S3Uploader,
    private val userRepository: UserRepository,
) {
    @Value("\${cloud.aws.s3.dir}")
    lateinit var dir: String

    // 게시판 홈
    fun getPostsInHome(userId: Long?): ResponseDto<PostsInHomeResponseDto> {
        val user = userRepository.findByIdOrNull(userId)
        val colleges = CollegeType.values()
        val userCollege = user?.major?.college ?: colleges[Random().nextInt(colleges.size)]

        val otherCollegePosts: MutableList<PostsInHomeResponseDto.CollegePosts> = mutableListOf()
        var userCollegePosts: PostsInHomeResponseDto.CollegePosts? = null
        for (college in colleges) {
            val collegePosts = PostsInHomeResponseDto.CollegePosts(
                collegeType = college,
                collegeTypeName = college.desc,
                posts = postRepository.findAllByCollegeAndActiveStatusOrderByCreatedAtDesc(
                    college,
                    ActiveStatusType.ACTIVE,
                    PageRequest.of(0, 5)
                ).map {
                    PostsInHomeResponseDto.PostHomeDto(
                        postId = it.id!!,
                        majorName = it.author.major.majorName,
                        postTitle = it.title,
                        postCreatedAt = it.createdAt
                    )
                }
            )
            if (userCollege == college) {
                userCollegePosts = collegePosts
            } else {
                otherCollegePosts.add(collegePosts)
            }
        }

        return ResponseDto(
            PostsInHomeResponseDto(userCollegePosts!!, otherCollegePosts),
            HttpStatus.OK.value(),
        )
    }

    fun searchPostsByCollege(searchPostsByCollege: SearchPostsByCollege): PagingResponseDto<SearchPostsByCollegeResponseDto> {
        // Best 게시글
        val collegeBestPosts = postLikeRepository.getMostLikedPostsInCollege(searchPostsByCollege.college.name)

        // 학과 전체 게시글
        val allPosts =
            qPostRepository.searchPostsByCollege(searchPostsByCollege.college, searchPostsByCollege.pageable!!)

        return PagingResponseDto(
            allPosts.totalElements,
            allPosts.totalPages,
            SearchPostsByCollegeResponseDto(
                bestPost = collegeBestPosts.map { updatePostDto(PostDto(it), searchPostsByCollege.user!!.id!!) },
                posts = allPosts.content.map {
                    // 이미지, 좋아요, 댓글 개수 업데이트
                    updatePostDto(it, searchPostsByCollege.user!!.id!!)
                }
            )
        )
    }


// 전체 게시글 조회
//    fun getAllPosts(pageable: Pageable, user: User?): List<PostDto> {
//        val userId: Long? = user?.id
//        val allPosts: List<Post> = postRepository.findAllByActiveStatusOrderByIdDesc(ActiveStatusType.ACTIVE)
//
//        // 게시글이 없을 경우 빈 배열
//        if (allPosts.isEmpty()) {
//            return emptyList()
//        }
//
//        val postsDto: MutableList<PostDto> = arrayListOf()
//        if (allPosts.isNotEmpty()) {
//            for (post in allPosts) {
//                val dto: PostDto = updatePostSummaryResponseDto(post, userId!!)
//                postsDto.add(dto)
//            }
//        }
//
//        val start: Long = pageable.offset
//        val end: Long = if ((start + pageable.pageSize) > postsDto.size) postsDto.size.toLong() else (start + pageable.pageSize)
//        return PageImpl(postsDto.subList(start.toInt(), end.toInt()), pageable, postsDto.size.toLong()).toList()
//    }

    // 단과대 별 게시글 조회
//    fun getAllCollegePosts(college: CollegeType, pageable: Pageable, user: User?): SearchPostsByCollegeResponseDto {
//        val userId: Long? = user?.id
//        val allPosts: List<Post> = postRepository.findAllByCollegeAndActiveStatusOrderByIdDesc(college, ActiveStatusType.ACTIVE)
//
//        // 게시글이 없을 경우 null + 빈 배열
//        if (allPosts.isEmpty()) {
//            return SearchPostsByCollegeResponseDto(null, emptyList())
//        }
//        // 해당 단과대의 베스트 게시글 ID
//        val collegeBestId: Long = postLikeRepository.getMostLikedPostInCollege(college.toString()) ?: 0
//        val best: Post = postRepository.findById(collegeBestId).orElse(allPosts[0])
//
//        // Best 게시글
//        val bestDto: PostDto = this.updatePostDto(best, userId!!)
//
//        // 게시글 리스트
//        val postsDto: MutableList<PostDto> = arrayListOf()
//        for (post in allPosts) {
//            val postDto: PostDto = this.updatePostDto(post, userId)
//            postsDto.add(postDto)
//        }
//
//        val start: Long = pageable.offset
//        val end: Long = if ((start + pageable.pageSize) > postsDto.size) postsDto.size.toLong() else (start + pageable.pageSize)
//        val postsDtoToPageable: List<PostDto> = PageImpl(postsDto.subList(start.toInt(), end.toInt()), pageable, postsDto.size.toLong()).toList()
//
//        return SearchPostsByCollegeResponseDto(bestDto, postsDtoToPageable)
//    }

    // 게시글 상세 조회하기
    fun getPostDetails(postId: Long, user: User): PostDetailResponseDto {
        val userId = user.id!!
        val post: Post = postRepository.findByIdAndActiveStatus(postId, ActiveStatusType.ACTIVE)
            ?: throw NotFoundException()

        val postDetailDto = PostDetailDto(post)
        postDetailDto.checkIsAnonymous()

        val postImages = postImageRepository.findAllByPostIdAndDeletedAtNull(post.id!!)
        if (postImages != null) {
            for (postImage in postImages) {
                postDetailDto.postImages.add(postImage.imageUrl)
            }
        }
        postDetailDto.postLikeCnt = postLikeRepository.countByPostIdAndStatus(post.id, ActiveStatusType.ACTIVE)
        postDetailDto.postCommentCnt = commentRepository.countByPostIdAndActiveStatus(post.id, ActiveStatusType.ACTIVE)
        postDetailDto.isLikedByMe =
            postLikeRepository.existsByPostIdAndUserIdAndStatus(post.id, userId, ActiveStatusType.ACTIVE)
        println(postLikeRepository.existsByPostIdAndUserIdAndStatus(post.id, userId, ActiveStatusType.ACTIVE))

        // TODO: 1. 댓글, 대댓글 넣기
        val comments = commentRepository.findByPostAndParentCommentNullOrderByCreatedAtAsc(post)
        val commentsDto: MutableList<PostDetailCommentDto> = mutableListOf()
        for (comment in comments) {
            val postDetailCommentDto = updatePostDetailCommentDto(comment, userId)
            commentsDto.add(postDetailCommentDto)
        }

        return PostDetailResponseDto(postDetailDto, commentsDto)
    }

    // 게시글 작성
    @Transactional
    fun createPost(postCreateDto: CreatePostRequestDto, user: User): CreatePostResponseDto {
        if (postCreateDto.college != user.major.college) {
            throw IllegalArgumentException("본인이 속한 학부 게시판에만 게시글 작성이 가능합니다.")
        }

        val post = postRepository.save(postCreateDto.toEntity(user))

        postCreateDto.file?.run {
            for (file in postCreateDto.file) {
                val fileUrl = s3Uploader.upload(file)
                val image = PostImage(post, fileUrl, file.originalFilename!!, fileUrl)
                postImageRepository.save(image)
            }
        }
        return CreatePostResponseDto(post.id!!)
    }

    // 게시글 수정
    @Transactional
    fun updatePost(postId: Long, postUpdateDto: UpdatePostRequestDto, user: User): PostDetailDto {
        val post = postRepository.findByIdOrNull(postId) ?: throw NotFoundException()
        print(postUpdateDto.isAnonymous)
        postUpdateDto.updateEntity(post)

        postUpdateDto.file?.run {
            for (file in postUpdateDto.file) {
                val fileUrl: String = s3Uploader.upload(file)
                val image = PostImage(post, fileUrl, file.originalFilename!!, fileUrl)
                postImageRepository.save(image)
            }
        }

        if (!postUpdateDto.deleteTargetUrl.isNullOrEmpty()) {
            for (target in postUpdateDto.deleteTargetUrl) {
                val targetImage = postImageRepository.findByImageUrlAndDeletedAtNull(target)
                    ?: throw NotFoundException()
                println(dir + targetImage.uploadName)
                println("asdasdasd");
//                s3Uploader.delete(targetImage.uploadName)
                val postImage = postImageRepository.findByImageUrl(targetImage.uploadName)
                postImage?.deletedAt = LocalDateTime.now();
            }
        }

        val updatedPost = postRepository.save(post)
        val postDetailDto = PostDetailDto(updatedPost)
        postDetailDto.checkIsAnonymous()

        val postImages = postImageRepository.findAllByPostIdAndDeletedAtNull(updatedPost.id!!)
        postImages?.run {
            for (postImage in postImages) {
                postDetailDto.postImages.add(postImage.imageUrl)
            }
        }

        postDetailDto.postLikeCnt = postLikeRepository.countByPostIdAndStatus(updatedPost.id, ActiveStatusType.ACTIVE)
        postDetailDto.postCommentCnt =
            commentRepository.countByPostIdAndActiveStatus(updatedPost.id, ActiveStatusType.ACTIVE)
        postDetailDto.isLikedByMe =
            postLikeRepository.existsByPostIdAndUserIdAndStatus(post.id!!, user.id!!, ActiveStatusType.ACTIVE)

        return postDetailDto
    }

    // 게시글 삭제
    @Transactional
    fun deletePost(postId: Long, user: User): DeletePostResponseDto {
        val post = postRepository.findByIdOrNull(postId) ?: throw Exception("해당하는 게시글이 없습니다.")
        if (post.author.id != user.id) {
            throw Exception("본인의 게시글만 삭제 가능합니다.")
        }
        post.activeStatus = ActiveStatusType.INACTIVE
        post.deletedAt = LocalDateTime.now()
        postRepository.save(post)

        return DeletePostResponseDto(true)
    }

    // 좋아요
    fun likePost(postId: Long, userId: Long): PostLikeResponseDto {
        val post = postRepository.findByIdOrNull(postId) ?: throw Exception("해당하는 게시글이 없습니다.")

        val postLike = postLikeRepository.findByPostAndUserId(post, userId)
        println(postLike)

        var isLikedByMe = false

        // 좋아요를 한 번도 누른적이 없으면, PostLike 추가
        if (postLike == null) {
            postLikeRepository.save(PostLike(post, userId))
            isLikedByMe = true
        } else {
            when (postLike.status) {
                ActiveStatusType.INACTIVE -> {
                    postLike.status = ActiveStatusType.ACTIVE
                    isLikedByMe = true
                }

                ActiveStatusType.ACTIVE -> postLike.status = ActiveStatusType.INACTIVE
            }
            postLikeRepository.save(postLike)
        }

        val postLikeCnt = postLikeRepository.countByPostIdAndStatus(postId, ActiveStatusType.ACTIVE)
        return PostLikeResponseDto(
            isLikedByMe,
            postLikeCnt,
        )
    }

    fun searchPost(searchPostRequest: SearchPostRequest, userId: Long): SearchPostResponse {
        val posts = qPostRepository.searchPostsByKeyWord(searchPostRequest)
        return SearchPostResponse(
            posts.content.map { post -> updatePostDto(post, userId) }
        )
    }

    private fun updatePostDto(postDto: PostDto, userId: Long): PostDto {
        postDto.postImageCnt = postImageRepository.countByPostId(postDto.postId!!)
        postDto.postLikeCnt = postLikeRepository.countByPostIdAndStatus(postDto.postId, ActiveStatusType.ACTIVE)
        postDto.postCommentCnt = commentRepository.countByPostIdAndActiveStatus(postDto.postId, ActiveStatusType.ACTIVE)
        postDto.isLikedByMe =
            postLikeRepository.existsByPostIdAndUserIdAndStatus(postDto.postId, userId, ActiveStatusType.ACTIVE)

        return postDto
    }

    private fun updatePostDetailCommentDto(comment: Comment, userId: Long): PostDetailCommentDto {
        val postDetailCommentDto = PostDetailCommentDto(comment)

        postDetailCommentDto.commentLikeCnt =
            commentLikeRepository.countByCommentIdAndActiveStatus(comment.id!!, ActiveStatusType.ACTIVE)
        postDetailCommentDto.isLikedByMe =
            commentLikeRepository.existsByIdAndUserIdAndActiveStatus(comment.id, userId, ActiveStatusType.ACTIVE)

        // reply
        val replies = commentRepository.findByParentComment(comment)
        val replyDtoList: MutableList<PostDetailReplyDto> = arrayListOf()
        for (reply in replies) {
            val replyDto: PostDetailReplyDto = this.updatePostDetailReplyDto(reply, userId)

            replyDtoList.add(replyDto)
        }

        postDetailCommentDto.replies = replyDtoList

        return postDetailCommentDto
    }

    private fun updatePostDetailReplyDto(reply: Comment, userId: Long): PostDetailReplyDto {
        val postDetailReplyDto = PostDetailReplyDto(reply)

        postDetailReplyDto.replyLikeCnt =
            commentLikeRepository.countByCommentIdAndActiveStatus(reply.id!!, ActiveStatusType.ACTIVE)
        postDetailReplyDto.isLikedByMe =
            commentLikeRepository.existsByIdAndUserIdAndActiveStatus(reply.id, userId, ActiveStatusType.ACTIVE)

        return postDetailReplyDto
    }
}