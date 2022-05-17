package team.waggly.backend.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import team.waggly.backend.service.emailService.CertificationService
import team.waggly.backend.service.emailService.SendEmailService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.dto.CertificationDto
import team.waggly.backend.dto.SendEmailDto
import team.waggly.backend.dto.SignupDto
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.SignupService

@RestController
class UserController(
    private val signupService: SignupService,
    private val sendEmailService: SendEmailService,
    private val certificationService: CertificationService
    )
{
    @PostMapping("/user/signup")
    fun signupController(@RequestBody signupRequestDto: SignupDto.Request): ResponseEntity<SignupDto.Response> {
        return ResponseEntity.ok().body(signupService.userSignup(signupRequestDto))
    }

    @GetMapping("/user/profile")
    fun getUserProfile(@AuthenticationPrincipal userDetailsImpl: UserDetailsImpl): ResponseEntity<SignupDto.Response>  {

        return ResponseEntity.ok().body(SignupDto.Response(true))
    }

    @PostMapping("/user/email")
    fun sendEmailController(@RequestBody emailCertificationRequestDto: SendEmailDto.Request): ResponseEntity<SendEmailDto.Response>{
        return ResponseEntity.ok().body(sendEmailService.emailCertification(emailCertificationRequestDto))
    }

    @PostMapping("/user/email/certification")
    fun certificationController(@RequestBody certificationRequestDto: CertificationDto.Reqeust): ResponseEntity<CertificationDto.Response> {
        return ResponseEntity.ok().body(certificationService.certificationEmail(certificationRequestDto))
    }
}