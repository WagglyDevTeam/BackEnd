package team.waggly.backend.service

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.dto.CreatePostDto
import team.waggly.backend.model.Post
import team.waggly.backend.repository.PostRepository

@Service
class PostService (
    private val postRepository: PostRepository
){
    fun getAllPosts(): MutableList<Post> {
        return postRepository.findAll()
    }

    fun createPost(createPostDto: CreatePostDto): Post {
        return postRepository.save(createPostDto.toEntity())
    }

    fun updatePost(updatePostDto: UpdatePostDto): Post {
        return postRepository.save(updatePostDto.toEntity())
    }

    fun deletePost(postId: Long) {
        val post = postRepository.findByIdOrNull(postId) ?: throw Exception("해당하는 게시글이 없습니다.")
        return postRepository.delete(post)
    }
}