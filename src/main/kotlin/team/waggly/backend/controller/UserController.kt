package team.waggly.backend.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.dto.SignupDto
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.SignupService

@RestController
class UserController(private val signupService: SignupService) {
    @PostMapping("/user/signup")
    fun signupController(@RequestBody signupRequestDto: SignupDto.Request): ResponseEntity<SignupDto.Response> {
        return ResponseEntity.ok().body(signupService.userSignup(signupRequestDto))
    }

    @GetMapping("/user/profile")
    fun getUserProfile(@AuthenticationPrincipal userDetailsImpl: UserDetailsImpl): ResponseEntity<SignupDto.Response>  {

        return ResponseEntity.ok().body(SignupDto.Response(true))
    }
}