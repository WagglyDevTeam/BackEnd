package team.waggly.backend.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.dto.CertificationDto
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.SendEmailDto
import team.waggly.backend.dto.SignupDto
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.SignupService
import team.waggly.backend.service.emailService.CertificationService
import team.waggly.backend.service.emailService.SendEmailService

@RestController
class UserController(
        private val signupService: SignupService,
        private val sendEmailService: SendEmailService,
        private val certificationService: CertificationService
) {
    @PostMapping("/user/signup")
    fun signupController(@RequestBody signupRequestDto: SignupDto.Request): ResponseDto<SignupDto.Response> {
        return ResponseDto(signupService.userSignup(signupRequestDto))
    }

    @GetMapping("/user/profile")
    fun getUserProfile(@AuthenticationPrincipal userDetailsImpl: UserDetailsImpl): ResponseDto<SignupDto.Response> {

        return ResponseDto(SignupDto.Response(true))
    }

    @PostMapping("/user/email")
    fun sendEmailController(@RequestBody emailCertificationRequestDto: SendEmailDto.Request): ResponseDto<SendEmailDto.Response> {
        return ResponseDto(sendEmailService.emailCertification(emailCertificationRequestDto))
    }

    @PostMapping("/user/email/certification")
    fun certificationController(@RequestBody certificationRequestDto: CertificationDto.Reqeust): ResponseDto<CertificationDto.Response> {
        return ResponseDto(certificationService.certificationEmail(certificationRequestDto))
    }
}