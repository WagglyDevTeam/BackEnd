package team.waggly.backend.service

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
import java.util.*
import javax.transaction.Transactional

@Service
class PostService (
    private val postRepository: PostRepository
){

    // 전체 게시글 조회
    // 어떻게 처리할지 모르므로 일단 DTO 없이 그냥 줘봄
    fun getAllPosts(pageable: Pageable): Page<Post>? {
        return postRepository.findAllByOrderByIdDesc(pageable) ?: Page.empty() // Pageable
    }

    // 단과대 별 게시글 조회
    fun getAllPostsByCollegeByOrderByIdDesc(college: String, pageable: Pageable): Page<Post>? {
        return postRepository.findAllByCollegeByOrderByIdDesc(college, pageable)
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