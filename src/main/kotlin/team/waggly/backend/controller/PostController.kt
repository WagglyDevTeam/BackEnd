package team.waggly.backend.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.service.PostService

@RestController
class PostController (
        private val postService: PostService
) {
    @GetMapping
    fun getAllPosts() = postService.getAllPosts()

    @PostMapping
    fun createPost() = postService.createPost()

    @DeleteMapping
    fun deletePost() = postService.deletePost()
}
