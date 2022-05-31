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
import team.waggly.backend.dto.user.CheckNicknameRequestDto
import team.waggly.backend.dto.user.CheckNicknameResponseDto
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.SignupService
import java.util.regex.Pattern
import javax.validation.Valid

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

    @PostMapping("/user/nickname")
    fun checkUserNickname(@RequestBody @Valid checkNicknameRequestDto: CheckNicknameRequestDto): CheckNicknameResponseDto {
        val pattern = Pattern.compile("^[A-Za-z0-9가-힣]{2,6}$")
        val matcher = pattern.matcher(checkNicknameRequestDto.nickname)
        if(!matcher.find())
            throw Exception("정규 표현식이 맞지 않습니다.")
        return signupService.checkUserNickname(checkNicknameRequestDto)
    }
}