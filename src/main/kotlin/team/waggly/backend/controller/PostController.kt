package team.waggly.backend.controller

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.waggly.backend.dto.PostCreateDto
import team.waggly.backend.dto.PostUpdateDto
import team.waggly.backend.model.User
import team.waggly.backend.service.PostService

@RestController
class PostController (
        private val postService: PostService
) {
    @GetMapping("/post")
    fun getAllPosts(
        @RequestParam(required = false, defaultValue = "1") page: String?,
        @RequestParam(required = false, defaultValue = "10") perPage: String?,
    ) = postService.getAllPosts(page as Int, perPage as Int) // Pageable 만들기

    @GetMapping("/post")
    fun getAllPostsByFilter(@RequestParam(required = false, defaultValue = "") college: String?,
                    @RequestParam(required = false, defaultValue = "1") page: String?,
                    @RequestParam(required = false, defaultValue = "10") perPage: String?,) {
        postService.getFilteredPosts(college, page, perPage)  // Pageable 만들기
    }

    @GetMapping("/post/{college}")
    fun getAllPostsByCollege(@RequestParam college: String) {
        postService.getAllPostsByCollege(college)
    }

    @PostMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.CREATED)  // 201
    fun createPost(@RequestBody postCreateDto: PostCreateDto, @AuthenticationPrincipal user: User) = postService.createPost(postCreateDto, user)

    @PutMapping
    @ResponseStatus(HttpStatus.OK)  // 200
    fun updatePost(@PathVariable postId: Long, @RequestBody postUpdateDto: PostUpdateDto) = postService.updatePost(postId, postUpdateDto)

    @DeleteMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // 204
    fun deletePost(@PathVariable postId: Long) = postService.deletePost(postId)
}
