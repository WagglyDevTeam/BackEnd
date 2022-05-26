package team.waggly.backend.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.model.User
import team.waggly.backend.repository.CommentRepository
import team.waggly.backend.repository.MajorRepository
import team.waggly.backend.repository.PostRepository
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.MyPageService

@RestController
class MyPageController (
    private val myPageService: MyPageService
        ) {
    @GetMapping("/user/post")
    fun getAllMyPost(@PageableDefault(size = 10, page = 0) pageable: Pageable,
                     @RequestParam college: String?,
                     @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl?): ResponseEntity<Any> {
        val user: User =
        myPageService.getAllMyPost()
        return ResponseEntity<Any>("", HttpStatus.OK)
    }
}