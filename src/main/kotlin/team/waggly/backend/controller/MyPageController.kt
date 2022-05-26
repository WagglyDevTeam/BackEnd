package team.waggly.backend.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.repository.CommentRepository
import team.waggly.backend.repository.MajorRepository
import team.waggly.backend.repository.PostRepository
import team.waggly.backend.security.UserDetailsImpl

@RestController
class MyPageController (
    private val postRepository: PostRepository,
    private val majorRepository: MajorRepository,
    private val commentRepository: CommentRepository,
        ) {
    @GetMapping("/user/post")
    fun getAllMyPostfun(@PageableDefault(size = 10, page = 0) pageable: Pageable,
                        @RequestParam college: String?,
                        @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl?): ResponseEntity<Any> {

    }
}