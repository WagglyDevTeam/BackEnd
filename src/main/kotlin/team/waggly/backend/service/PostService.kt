package team.waggly.backend.service

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.dto.PostDto
import team.waggly.backend.model.Post
import team.waggly.backend.model.User
import team.waggly.backend.repository.CommentRepository
import team.waggly.backend.repository.PostLikeRepository
import team.waggly.backend.repository.PostRepository
import team.waggly.backend.security.UserDetailsImpl
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class PostService (
    private val postRepository: PostRepository,
    private val postLikeRepository: PostLikeRepository,
    private val commentRepository: CommentRepository
){

    // 전체 게시글 조회
    fun getAllPosts(pageable: Pageable, user: User?): List<PostDto.PostDetailsResponseDto> {
        val userId = user?.id ?: null
        val allPosts: List<Post> = postRepository.findAll()
        val postsDto: List<PostDto.PostDetailsResponseDto> = allPosts.map { posts ->
            PostDto.PostDetailsResponseDto(posts, postLikeRepository, commentRepository, userId)
        }
        val postsDtoToPageable: Page<PostDto.PostDetailsResponseDto> =
            PageImpl<PostDto.PostDetailsResponseDto>(postsDto, pageable, postsDto.size.toLong())
        return postsDtoToPageable.toList()
    }

    // 게시글 상세 조회하기
    fun getPostDetails(postId: Long, user: User): PostDto.PostDetailsResponseDto {
        if (user.id == null) {
            throw NotFoundException()
        }
        val post: Post = postRepository.findById(postId).orElseThrow()
        return PostDto.PostDetailsResponseDto(post, postLikeRepository, commentRepository, user.id)
    }

    // 단과대 별 게시글 조회
    // 어떻게 처리할지 모르므로 일단 DTO 없이 그냥 줘봄
    fun getAllPostsByCollegeByOrderByIdDesc(college: CollegeType, pageable: Pageable, user: User?): PostDto.CollegePostsResponseDto {
        val userId = user?.id ?: null
        // 해당 단과대의 베스트 게시글 ID
        val allPosts: List<Post> = postRepository.findByCollegeByOrderByIdDesc(college.toString())
        val collegeBestId: Long = postLikeRepository.getMostLikedPostInCollege(college.toString()) ?: 0
        val best: Post = postRepository.findById(collegeBestId).orElse(allPosts[0])

        val bestDto: PostDto.PostDetailsResponseDto = PostDto.PostDetailsResponseDto(best, postLikeRepository, commentRepository, userId)
        println("bestDto: $bestDto")
        val postsDto: List<PostDto.PostDetailsResponseDto> = allPosts.map {
                posts -> PostDto.PostDetailsResponseDto(posts, postLikeRepository, commentRepository, userId)
        }
        println("postDto: $postsDto")
        val postsDtoToPageable: Page<PostDto.PostDetailsResponseDto> = PageImpl<PostDto.PostDetailsResponseDto>(postsDto, pageable, postsDto.size.toLong())
        val postsPageableToList: List<PostDto.PostDetailsResponseDto> = postsDtoToPageable.toList()

        return PostDto.CollegePostsResponseDto(bestDto, postsPageableToList)
    }

    @Transactional
    fun createPost(postCreateDto: PostDto.CreatePostRequestDto, userDetailsImpl: UserDetailsImpl): PostDto.CreatePostResponseDto {
        val user = userDetailsImpl.user
        if (user.id == null) {
            throw NotFoundException()
        }
        postRepository.save(postCreateDto.toEntity(user))
        return PostDto.CreatePostResponseDto(true)
    }

    @Transactional
    fun updatePost(postId: Long, postUpdateDto: PostDto.UpdatePostRequestDto): Post {
        val post: Post = postRepository.findByIdOrNull(postId) ?: throw NotFoundException()
        postUpdateDto.updateEntity(post)
        return postRepository.save(post)
    }

    @Transactional
    fun deletePost(postId: Long): PostDto.DeletePostResponseDto {
        val post: Post = postRepository.findByIdOrNull(postId) ?: throw Exception("해당하는 게시글이 없습니다.")
        post.activeStatus = ActiveStatusType.INACTIVE
        post.deletedAt = LocalDateTime.now()
        postRepository.save(post)

        return PostDto.DeletePostResponseDto(true)
    }
}