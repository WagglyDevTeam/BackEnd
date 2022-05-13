package team.waggly.backend.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.waggly.backend.dto.PostCreateDto
import team.waggly.backend.dto.PostUpdateDto
import team.waggly.backend.model.Post
import team.waggly.backend.model.User
import team.waggly.backend.service.PostService
import java.util.*

@RestController
class PostController (
        private val postService: PostService
) {
    @GetMapping("/post")
    fun getAllPosts(@PageableDefault(size = 10, page = 0) pageable: Pageable): Page<Post>? {
        return postService.getAllPosts(pageable)
    }

    @GetMapping("/post/{college}")
    fun getAllPostsByCollege(@RequestParam college: String,
                             @PageableDefault(size = 10, page = 0) pageable: Pageable): Page<Post>? {
        return postService.getAllPostsByCollegeByOrderByIdDesc(college, pageable)
    }

    @PostMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.CREATED)  // 201
    fun createPost(@RequestBody postCreateDto: PostCreateDto, @AuthenticationPrincipal user: User) = postService.createPost(postCreateDto, user)

    @PutMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.OK)  // 200
    fun updatePost(@PathVariable postId: Long, @RequestBody postUpdateDto: PostUpdateDto) = postService.updatePost(postId, postUpdateDto)

    @DeleteMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // 204
    fun deletePost(@PathVariable postId: Long) = postService.deletePost(postId)
}
