package team.waggly.backend.service

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.dto.PostCreateDto
import team.waggly.backend.dto.PostUpdateDto
import team.waggly.backend.model.Post
import team.waggly.backend.model.User
import team.waggly.backend.repository.PostRepository
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class PostService (
    private val postRepository: PostRepository
){
    fun getAllPosts(page: Int, perPage: Int): MutableList<Post> {
        return postRepository.findAll()  // Pageable
    }

    fun getAllPostsByCollege(college: String) {
        if (CollegeType.valueOf(college) === null) {
            println(college)
        }
//        val bestPost: Post = postRepository.findAllByCollege(college)
    }

    @Transactional
    fun createPost(postCreateDto: PostCreateDto, user: User): Post {
        return postRepository.save(postCreateDto.toEntity(user))
    }

    @Transactional
    fun updatePost(postId: Long, postUpdateDto: PostUpdateDto): Post {
        val post: Post = postRepository.findByIdOrNull(postId) ?: throw NotFoundException()
        postUpdateDto.updateEntity(post)
        return postRepository.save(post)
    }

    @Transactional
    fun deletePost(postId: Long) {
        val post: Post = postRepository.findByIdOrNull(postId) ?: throw Exception("해당하는 게시글이 없습니다.")
        post.activeStatus = ActiveStatusType.INACTIVE
        post.deletedAt = LocalDateTime.now()
        postRepository.save(post)
    }

    fun getFilteredPosts(college: String?, page: String?, perPage: String?) {

    }
}