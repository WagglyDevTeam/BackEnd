package team.waggly.backend.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.dto.postDto.*
import team.waggly.backend.model.Post
import team.waggly.backend.model.PostImage
import team.waggly.backend.model.User
import team.waggly.backend.repository.CommentRepository
import team.waggly.backend.repository.PostImageRepository
import team.waggly.backend.repository.PostLikeRepository
import team.waggly.backend.repository.PostRepository
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.awsS3.S3Uploader
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional

@Service
class PostService (
        private val postRepository: PostRepository,
        private val postLikeRepository: PostLikeRepository,
        private val commentRepository: CommentRepository,
        private val postImageRepository: PostImageRepository,
        private val s3Uploader: S3Uploader,
){
    @Value("\${cloud.aws.s3.dir}")
    lateinit var dir: String

    // 전체 게시글 조회
    fun getAllPosts(pageable: Pageable, user: User?): List<PostDetailsResponseDto> {
        val userId: Long? = user?.id
        val allPosts: List<Post> = postRepository.findAllActivePosts()

        val postsDto: MutableList<PostDetailsResponseDto> = arrayListOf()
        for (post in allPosts) {
            val dto: PostDetailsResponseDto = updatePostDetailInfo(post, userId!!)
            postsDto.add(dto)
        }

        val start: Long = pageable.offset
        val end: Long = if ((start + pageable.pageSize) > postsDto.size) postsDto.size.toLong() else (start + pageable.pageSize)
        return PageImpl(postsDto.subList(start.toInt(), end.toInt()), pageable, postsDto.size.toLong()).toList()
    }

    // 단과대 별 게시글 조회
    fun getAllPostsByCollegeByOrderByIdDesc(college: CollegeType, pageable: Pageable, user: User?): CollegePostsResponseDto {
        val userId: Long? = user?.id
        val allPosts: List<Post> = postRepository.findActivePostsByCollegeByOrderByIdDesc(college.toString())

        // 해당 단과대의 베스트 게시글 ID
        val collegeBestId: Long = postLikeRepository.getMostLikedPostInCollege(college.toString()) ?: 0
        val best: Post = postRepository.findById(collegeBestId).orElse(allPosts[0])

        // Best 게시글
        val bestDto: PostDetailsResponseDto = this.updatePostDetailInfo(best, userId!!)

        // 게시글 리스트
        val postsDto: MutableList<PostDetailsResponseDto> = arrayListOf()
        for (post in allPosts) {
            val postDto: PostDetailsResponseDto = this.updatePostDetailInfo(post, userId!!)
            postsDto.add(postDto)
        }

        val start: Long = pageable.offset
        val end: Long = if ((start + pageable.pageSize) > postsDto.size) postsDto.size.toLong() else (start + pageable.pageSize)
        val postsDtoToPageable: List<PostDetailsResponseDto> = PageImpl(postsDto.subList(start.toInt(), end.toInt()), pageable, postsDto.size.toLong()).toList()

        return CollegePostsResponseDto(bestDto, postsDtoToPageable)
    }

    // 게시글 상세 조회하기 (만약 본인 게시글이면 조회가 가능해야하니깐)
    fun getPostDetails(postId: Long, user: User?): PostDetailsResponseDto {
        val userId: Long? = user?.id

        val post: Post = postRepository.findById(postId).orElseThrow()

        if (post.activeStatus != ActiveStatusType.ACTIVE) {
            throw IllegalArgumentException("비공개 게시글입니다.")
        }
        val postDetailsResponseDto = PostDetailsResponseDto(post)

        postDetailsResponseDto.postImageCnt = postImageRepository.findAllByPostId(post.id!!).size
        postDetailsResponseDto.postLikeCnt = postLikeRepository.countByPostId(post.id!!)
        postDetailsResponseDto.postCommentCnt = commentRepository.countByPostId(post.id!!)
        postDetailsResponseDto.isLikedByMe = if (userId != null) postLikeRepository.existsByUserId(userId!!) else false

        return postDetailsResponseDto
    }

    @Transactional
    fun createPost(postCreateDto: CreatePostRequestDto,
                   userDetailsImpl: UserDetailsImpl): CreatePostResponseDto {
        val user = userDetailsImpl.user
        if (user.id == null) {
            throw NotFoundException()
        }
        val post: Post = postCreateDto.toEntity(user)
        postRepository.save(post)

        if (postCreateDto.file != null) {
            for (file in postCreateDto.file) {
                val extName: String = file.originalFilename!!.substringAfterLast(".")
                val uploadName = "${UUID.randomUUID()}.${extName}"
                val fileUrl: String = s3Uploader.upload(file!!, uploadName)
                val image = PostImage(post, fileUrl, file.originalFilename!!, uploadName)
                postImageRepository.save(image)
            }
        }
        return CreatePostResponseDto(true)
    }

    @Transactional
    fun updatePost(postId: Long,
                   postUpdateDto: UpdatePostRequestDto,
                   userDetailsImpl: UserDetailsImpl): PostDetailsResponseDto {
        val user = userDetailsImpl.user
        if (user.id == null) {
            throw NotFoundException()
        }
        val post: Post = postRepository.findByIdOrNull(postId) ?: throw NotFoundException()
        postUpdateDto.updateEntity(post)

        if (postUpdateDto.file != null) {
            for (file in postUpdateDto.file) {
                val extName: String = file.originalFilename!!.substringAfterLast(".")
                val uploadName = "${UUID.randomUUID()}.${extName}"
                val fileUrl: String = s3Uploader.upload(file!!, uploadName)
                val image = PostImage(post, fileUrl, file.originalFilename!!, uploadName)
                postImageRepository.save(image)
            }
        }

        if (postUpdateDto.deleteTargetId != null && postUpdateDto.deleteTargetId.isNotEmpty()) {
            for (target in postUpdateDto.deleteTargetId) {
                val targetImage: PostImage = postImageRepository.findById(target.toLong()).orElseThrow()
                println(dir + targetImage.uploadName)
                s3Uploader.delete(targetImage.uploadName)
                postImageRepository.deleteById(target.toLong())
            }
        }
        val updatedPost = postRepository.save(post)

        val postDetailsResponseDto = PostDetailsResponseDto(updatedPost)

        postDetailsResponseDto.postImageCnt = postImageRepository.findAllByPostId(updatedPost.id!!).size
        postDetailsResponseDto.postLikeCnt = postLikeRepository.countByPostId(updatedPost.id!!)
        postDetailsResponseDto.postCommentCnt = commentRepository.countByPostId(updatedPost.id!!)
        postDetailsResponseDto.isLikedByMe = if (user.id != null) postLikeRepository.existsByUserId(user.id) else false

        return postDetailsResponseDto
    }

    @Transactional
    fun deletePost(postId: Long, user: User): DeletePostResponseDto {
        val post: Post = postRepository.findByIdOrNull(postId) ?: throw Exception("해당하는 게시글이 없습니다.")
        if (post.author.id != user.id) {
            throw Exception("본인의 게시글만 삭제 가능합니다.")
        }
        post.activeStatus = ActiveStatusType.INACTIVE
        post.deletedAt = LocalDateTime.now()
        postRepository.save(post)

        return DeletePostResponseDto(true)
    }

    fun updatePostDetailInfo(post: Post, userId: Long): PostDetailsResponseDto {
        val postDetailsResponseDto = PostDetailsResponseDto(post)

        val postImageCnt: Int = postImageRepository.findAllByPostId(post.id!!).size
        val postLikeCnt: Int = postLikeRepository.countByPostId(post.id!!)
        val postCommentCnt: Int = commentRepository.countByPostId(post.id!!)
        val isLikedByMe: Boolean = if (userId != null) postLikeRepository.existsByUserId(userId) else false

        postDetailsResponseDto.postImageCnt = postImageCnt
        postDetailsResponseDto.postLikeCnt = postLikeCnt
        postDetailsResponseDto.postCommentCnt = postCommentCnt
        postDetailsResponseDto.isLikedByMe = isLikedByMe

        return postDetailsResponseDto
    }
}