package team.waggly.backend.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.dto.PostDto
import team.waggly.backend.model.Post
import team.waggly.backend.model.User
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.PostService

@RestController
class PostController (
        private val postService: PostService
) {
    @GetMapping("/post")
    fun getAllPosts(@PageableDefault(size = 10, page = 0) pageable: Pageable,
                    @RequestParam college: String?,
                    @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl?): Any? {
        val user: User? = userDetailsImpl?.user ?: null
        if (college == null) {
            return postService.getAllPosts(pageable, user)
        } else {
            val collegeEnum = when (college) {
                "test" -> CollegeType.TEST
                "artsports" -> CollegeType.ARTSPORTS
                "engineering" -> CollegeType.ENGINEERING
                "medical" -> CollegeType.MEDICAL
                "nature" -> CollegeType.NATURE
                "social" -> CollegeType.SOCIAL
                else -> throw NoSuchElementException("올바른 학부를 선택해주세요.")
            }

            return postService.getAllPostsByCollegeByOrderByIdDesc(collegeEnum, pageable, user)
        }
    }

    @GetMapping("/post/{postId}")
    fun getPostDetails(@PathVariable postId: Long,
                       @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl): PostDto.PostDetailsResponseDto {
        val user: User = userDetailsImpl.user
        return postService.getPostDetails(postId, user)
    }
//
//    @GetMapping("/post")
//    fun getAllPostsByCollege(@RequestParam college: String?,
//                             @PageableDefault(size = 10, page = 0) pageable: Pageable,
//                             @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl): PostDto.CollegePostsResponseDto {
//        if (college == null) {
//            throw IllegalArgumentException("해당하는 학부가 없습니다.")
//        }
//        val collegeEnum = when (college) {
//            "test" -> CollegeType.TEST
//            "artsports" -> CollegeType.ARTSPORTS
//            "engineering" -> CollegeType.ENGINEERING
//            "medical" -> CollegeType.MEDICAL
//            "nature" -> CollegeType.NATURE
//            "social" -> CollegeType.SOCIAL
//            else -> throw java.lang.IllegalArgumentException("올바른 학부를 선택해주세요.")
//        }
//
//        val user: User = userDetailsImpl.user
//        return postService.getAllPostsByCollegeByOrderByIdDesc(collegeEnum, pageable, user)
//    }

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)  // 201
    fun createPost(@RequestBody postCreateDto: PostDto.CreatePostRequestDto,
                   @AuthenticationPrincipal  userDetailsImpl: UserDetailsImpl)
    = postService.createPost(postCreateDto, userDetailsImpl)

    @PutMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.OK)  // 200
    fun updatePost(@PathVariable postId: Long, @RequestBody postUpdateDto: PostDto.UpdatePostRequestDto) = postService.updatePost(postId, postUpdateDto)

    @DeleteMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // 204
    fun deletePost(@PathVariable postId: Long) = postService.deletePost(postId)
}
