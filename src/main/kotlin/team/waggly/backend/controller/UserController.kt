package team.waggly.backend.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.waggly.backend.dto.CertificationDto
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.SendEmailDto
import team.waggly.backend.dto.SignupDto
import team.waggly.backend.dto.mypagedto.*
import team.waggly.backend.dto.user.CheckNicknameRequestDto
import team.waggly.backend.dto.user.CheckNicknameResponseDto
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.SignupService
import team.waggly.backend.service.UserService
import team.waggly.backend.service.emailService.CertificationService
import team.waggly.backend.service.emailService.SendEmailService
import java.util.regex.Pattern
import javax.validation.Valid

@RestController
class UserController(
    private val signupService: SignupService,
    private val sendEmailService: SendEmailService,
    private val certificationService: CertificationService,
    private val userService: UserService
) {
    @PostMapping("/user/signup")
    fun signupController(@RequestBody signupRequestDto: SignupDto.Request): ResponseDto<Any> {
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

    @PutMapping("/user/profile")
    fun updateUserProfile(
        @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
        @RequestBody updateUserProfileRequestDto: UpdateUserProfileRequestDto,
    ): ResponseDto<UpdateUserProfileDto> {
        val user = userDetailsImpl.user
        return ResponseDto(userService.updateUserProfile(user, updateUserProfileRequestDto))
    }

    @PutMapping("/user/introduction")
    fun updateUserIntroduction(
        @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
        @RequestBody updateUserIntroductionRequestDto: UpdateUserIntroductionRequestDto,
    ): ResponseDto<UpdateUserIntroductionDto> {
        val user = userDetailsImpl.user
        return ResponseDto(userService.updateUserIntroduction(user, updateUserIntroductionRequestDto))
    }

    @PostMapping("/user/password")
    fun checkPassword(
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
            @RequestBody checkPasswordRequestDto: PasswordRequestDto
    ): ResponseDto<Any> {
        val user = userDetailsImpl.user
        return ResponseDto(userService.checkPassword(user, checkPasswordRequestDto))
    }

    @PutMapping("/user/password")
    fun updatePassword(
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
            @RequestBody updatePasswordRequestDto: PasswordRequestDto,
    ): ResponseDto<Any> {
        val user = userDetailsImpl.user
        return ResponseDto(userService.updatePassword(user, updatePasswordRequestDto))
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