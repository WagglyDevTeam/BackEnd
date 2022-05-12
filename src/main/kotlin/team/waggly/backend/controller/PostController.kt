package team.waggly.backend.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.dto.PostCreateDto
import team.waggly.backend.dto.PostUpdateDto
import team.waggly.backend.service.PostService

@RestController
class PostController (
        private val postService: PostService
) {
    @GetMapping("/post")
    fun getAllPosts() = postService.getAllPosts()

    @GetMapping("/post/{postId}")

    @PostMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.CREATED)  // 201
    fun createPost(@RequestBody postCreateDto: PostCreateDto) = postService.createPost(postCreateDto)

    @PutMapping
    @ResponseStatus(HttpStatus.OK)  // 200
    fun updatePost(@RequestParam postId: Long, @RequestBody postUpdateDto: PostUpdateDto) = postService.updatePost(postId, postUpdateDto)

    @DeleteMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // 204
    fun deletePost(@RequestParam postId: Long) = postService.deletePost(postId)
}
