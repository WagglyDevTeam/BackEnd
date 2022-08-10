package team.waggly.backend.service

import com.uwyn.jhighlight.fastutil.Hash
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.dto.postDto.*
import team.waggly.backend.model.*
import team.waggly.backend.repository.*
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.awsS3.S3Uploader
import team.waggly.backend.service.tika.TikaService
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class PostService(
        private val postRepository: PostRepository,
        private val postLikeRepository: PostLikeRepository,
        private val commentRepository: CommentRepository,
        private val postImageRepository: PostImageRepository,
        private val commentLikeRepository: CommentLikeRepository,
        private val hashtagRepository: HashTagRepository,
        private val s3Uploader: S3Uploader,
        private val tikaService: TikaService,
) {
    @Value("\${cloud.aws.s3.dir}")
    lateinit var dir: String

    // 전체 게시글 조회
    fun getAllPosts(pageable: Pageable, user: User?): List<PostSummaryResponseDto> {
        val userId: Long? = user?.id
        val allPosts: List<Post> = postRepository.findAllByActiveStatusOrderByIdDesc(ActiveStatusType.ACTIVE)

        // 게시글이 없을 경우 빈 배열
        if (allPosts.isEmpty()) {
            return emptyList()
        }

        val postsDto: MutableList<PostSummaryResponseDto> = arrayListOf()
        if (allPosts.isNotEmpty()) {
            for (post in allPosts) {
                val dto: PostSummaryResponseDto = updatePostSummaryResponseDto(post, userId!!)
                postsDto.add(dto)
            }
        }

        val start: Long = pageable.offset
        val end: Long = if ((start + pageable.pageSize) > postsDto.size) postsDto.size.toLong() else (start + pageable.pageSize)
        return PageImpl(postsDto.subList(start.toInt(), end.toInt()), pageable, postsDto.size.toLong()).toList()
    }

    // 단과대 별 게시글 조회
    fun getAllCollegePosts(college: CollegeType, pageable: Pageable, user: User?): CollegePostsResponseDto {
        val userId: Long? = user?.id
        val allPosts: List<Post> = postRepository.findAllByCollegeAndActiveStatusOrderByIdDesc(college, ActiveStatusType.ACTIVE)

        // 게시글이 없을 경우 null + 빈 배열
        if (allPosts.isEmpty()) {
            return CollegePostsResponseDto(null, emptyList())
        }
        // 해당 단과대의 베스트 게시글 ID
        val collegeBestId: Long = postLikeRepository.getMostLikedPostInCollege(college.toString()) ?: 0
        val best: Post = postRepository.findById(collegeBestId).orElse(allPosts[0])

        // Best 게시글
        val bestDto: PostSummaryResponseDto = this.updatePostSummaryResponseDto(best, userId!!)

        // 게시글 리스트
        val postsDto: MutableList<PostSummaryResponseDto> = arrayListOf()
        for (post in allPosts) {
            val postDto: PostSummaryResponseDto = this.updatePostSummaryResponseDto(post, userId)
            postsDto.add(postDto)
        }

        val start: Long = pageable.offset
        val end: Long = if ((start + pageable.pageSize) > postsDto.size) postsDto.size.toLong() else (start + pageable.pageSize)
        val postsDtoToPageable: List<PostSummaryResponseDto> = PageImpl(postsDto.subList(start.toInt(), end.toInt()), pageable, postsDto.size.toLong()).toList()

        return CollegePostsResponseDto(bestDto, postsDtoToPageable)
    }

    // 게시글 상세 조회하기
    fun getPostDetails(postId: Long, user: User?): PostDetailResponseDto {
        val userId: Long? = user?.id
        val post: Post = postRepository.findById(postId).orElse(null) ?: throw NotFoundException()

        // 삭제된 게시글 처리
        if (post.activeStatus == ActiveStatusType.INACTIVE) {
            throw IllegalArgumentException("삭제된 게시글입니다.")
        }

        var postDetailDto = PostDetailDto(post)
        postDetailDto.hashtags = hashtagRepository.findAllByPostAndDeletedAtNull(post)
        print(postDetailDto.hashtags)

        val postImages = postImageRepository.findAllByPostIdAndDeletedAtNull(post.id!!)
        if (postImages != null) {
            for (postImage in postImages) {
                postDetailDto.postImages.add(postImage.imageUrl)
            }
        }
        postDetailDto.postLikeCnt = postLikeRepository.countByPostIdAndStatus(post.id, ActiveStatusType.ACTIVE)
        postDetailDto.postCommentCnt = commentRepository.countByPostId(post.id)
        postDetailDto.isLikedByMe = if (userId != null) postLikeRepository.existsByIdAndUserIdAndStatus(post.id, userId, ActiveStatusType.ACTIVE) else false

        // TODO: 1. 댓글, 대댓글 넣기
        val comments: List<Comment> = commentRepository.findByPostAndActiveStatusAndParentCommentNullOrderByCreatedAtAsc(post, ActiveStatusType.ACTIVE)
        val commentsDto: MutableList<PostDetailCommentDto> = arrayListOf()
        if (comments.isNotEmpty()) {
            for (comment in comments) {
                val dto: PostDetailCommentDto = updatePostDetailCommentDto(comment, userId!!)
                commentsDto.add(dto)
            }
        }

        return PostDetailResponseDto(postDetailDto, commentsDto)
    }

    // 게시글 작성
    @Transactional
    fun createPost(postCreateDto: CreatePostRequestDto,
                   userDetailsImpl: UserDetailsImpl): CreatePostResponseDto {
        val user = userDetailsImpl.user
        if (user.id == null) {
            throw NotFoundException()
        }

        if (postCreateDto.college != user.major.college) {
            throw IllegalArgumentException("본인이 속한 학부 게시판에만 게시글 작성이 가능합니다.")
        }

        val post: Post = postCreateDto.toEntity(user)
        postRepository.save(post)

        val hashtagString: List<String>? = postCreateDto.hashtags

        if (hashtagString != null && hashtagString.isNotEmpty()) {
            for (hashtag in hashtagString) {
                val newHashtag: HashTag = HashTag(
                    post = post,
                    hashTagName = hashtag)

             hashtagRepository.save(newHashtag)
            }
        }

        if (postCreateDto.file != null) {
            for (file in postCreateDto.file) {
                if (!tikaService.typeCheck(file)) {
                    throw IllegalArgumentException("올바른 파일 형식이 아닙니다. (.jpg, .jpeg, .gif, .png)")
                }
                val fileUrl: String = s3Uploader.upload(file, tikaService.mimeType(file))
                val image = PostImage(post, fileUrl, file.originalFilename!!, fileUrl)
                postImageRepository.save(image)
            }
        }
        return CreatePostResponseDto(true)
    }

    // 게시글 수정
    @Transactional
    fun updatePost(postId: Long,
                   postUpdateDto: UpdatePostRequestDto,
                   userDetailsImpl: UserDetailsImpl): PostDetailDto {
        val user = userDetailsImpl.user
        if (user.id == null) {
            throw NotFoundException()
        }
        val post: Post = postRepository.findByIdOrNull(postId) ?: throw NotFoundException()
        postUpdateDto.updateEntity(post)

        if (postUpdateDto.file != null) {
            for (file in postUpdateDto.file) {
                if (!tikaService.typeCheck(file)) {
                    throw IllegalArgumentException("올바른 파일 형식이 아닙니다. (.jpg, .jpeg, .gif, .png)")
                }

                val fileUrl: String = s3Uploader.upload(file, tikaService.mimeType(file))
                val image = PostImage(post, fileUrl, file.originalFilename!!, fileUrl)
                postImageRepository.save(image)
            }
        }

        if ((postUpdateDto.deleteTargetUrl != null) && postUpdateDto.deleteTargetUrl.isNotEmpty()) {
            for (target in postUpdateDto.deleteTargetUrl) {
                val targetImage: PostImage = postImageRepository.findByImageUrlAndDeletedAtNull(target)
                        ?: throw NotFoundException()
                s3Uploader.delete(targetImage.uploadName)
                postImageRepository.delete(targetImage)
            }
        }
        // TODO: 게시글 수정할 때 해시태그 수정하는 로직 만들어야 함.

        val updatedPost = postRepository.save(post)
        val postDetailDto = PostDetailDto(updatedPost)

        val postImages = postImageRepository.findAllByPostIdAndDeletedAtNull(updatedPost.id!!)
        if (postImages != null) {
            for (postImage in postImages) {
                postDetailDto.postImages.add(postImage.imageUrl)
            }
        }

        postDetailDto.postLikeCnt = postLikeRepository.countByPostIdAndStatus(updatedPost.id, ActiveStatusType.ACTIVE)
        postDetailDto.postCommentCnt = commentRepository.countByPostId(updatedPost.id)
        postDetailDto.isLikedByMe = postLikeRepository.existsByIdAndUserIdAndStatus(post.id!!, user.id, ActiveStatusType.ACTIVE)

        return postDetailDto
    }

    // 게시글 삭제
    @Transactional
    fun deletePost(postId: Long, user: User): DeletePostResponseDto {
        val post: Post = postRepository.findByIdOrNull(postId) ?: throw Exception("해당하는 게시글이 없습니다.")
        if (post.author.id != user.id) {
            throw Exception("본인의 게시글만 삭제 가능합니다.")
        }
        post.activeStatus = ActiveStatusType.INACTIVE
        post.deletedAt = LocalDateTime.now()
        postRepository.save(post)

        // TODO: 게시글 삭제할 때 해시태그도 같이 삭제하는 로직 만들어야 함.

        return DeletePostResponseDto(true)
    }

    // 좋아요
    fun likePost(postId: Long, userId: Long): PostLikeResponseDto {
        val post: Post = postRepository.findById(postId).orElseThrow()

        val postLike: PostLike? = postLikeRepository.findByPostAndUserId(post, userId)

        var isLikedByMe: Boolean = false

        // 좋아요를 안눌렀으면, PostLike 추가
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

        val postLikeCnt: Int = postLikeRepository.countByPostIdAndStatus(postId, ActiveStatusType.ACTIVE)
        return PostLikeResponseDto(
                isLikedByMe,
                postLikeCnt,
        )
    }

    private fun updatePostSummaryResponseDto(post: Post, userId: Long): PostSummaryResponseDto {
        val postSummaryResponseDto = PostSummaryResponseDto(post)

        postSummaryResponseDto.postImageCnt = postImageRepository.countByPostId(post.id!!)
        postSummaryResponseDto.postLikeCnt = postLikeRepository.countByPostIdAndStatus(post.id, ActiveStatusType.ACTIVE)
        postSummaryResponseDto.postCommentCnt = commentRepository.countByPostId(post.id)
        postSummaryResponseDto.isLikedByMe = postLikeRepository.existsByIdAndUserIdAndStatus(post.id, userId, ActiveStatusType.ACTIVE)

        return postSummaryResponseDto
    }

    private fun updatePostDetailCommentDto(comment: Comment, userId: Long): PostDetailCommentDto {
        val postDetailCommentDto = PostDetailCommentDto(comment)

        postDetailCommentDto.commentLikeCnt = commentLikeRepository.countByCommentIdAndActiveStatus(comment.id!!, ActiveStatusType.ACTIVE)
        postDetailCommentDto.isLikedByMe = commentLikeRepository.existsByIdAndUserIdAndActiveStatus(comment.id, userId, ActiveStatusType.ACTIVE)

        // reply
        val replies = commentRepository.findByParentCommentAndActiveStatus(comment, ActiveStatusType.ACTIVE)

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

        postDetailReplyDto.replyLikeCnt = commentLikeRepository.countByCommentIdAndActiveStatus(reply.id!!, ActiveStatusType.ACTIVE)
        postDetailReplyDto.isLikedByMe = commentLikeRepository.existsByIdAndUserIdAndActiveStatus(reply.id, userId, ActiveStatusType.ACTIVE)

        return postDetailReplyDto
    }
}