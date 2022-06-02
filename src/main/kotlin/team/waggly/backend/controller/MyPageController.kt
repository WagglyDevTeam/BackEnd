package team.waggly.backend.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.myPageDto.MyCommentsResponseDto
import team.waggly.backend.dto.myPageDto.MyPostsResponseDto
import team.waggly.backend.model.User
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.MyPageService

@RestController
class MyPageController (
    private val myPageService: MyPageService
        ) {
    @GetMapping("/user/post")
    fun getAllMyPosts(@PageableDefault(size = 10, page = 0) pageable: Pageable,
                      @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl): ResponseDto<MyPostsResponseDto> {
        val user: User = userDetailsImpl.user

        return ResponseDto(myPageService.getAllMyPosts(pageable, user), HttpStatus.OK.value())
    }

    @GetMapping("/user/comment")
    fun getAllMyComments(@PageableDefault(size = 10, page = 0) pageable: Pageable,
                         @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl): ResponseDto<MyCommentsResponseDto> {
        val user: User = userDetailsImpl.user

        return ResponseDto(myPageService.getAllMyComments(pageable, user), HttpStatus.OK.value())
    }
}
