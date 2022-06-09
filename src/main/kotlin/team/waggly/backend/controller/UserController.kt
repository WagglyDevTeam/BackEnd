package team.waggly.backend.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.certification.CertificationRequestDto
import team.waggly.backend.dto.email.EmailRequestDto
import team.waggly.backend.dto.myPageDto.*
import team.waggly.backend.dto.user.CheckNicknameRequestDto
import team.waggly.backend.dto.user.SignupReqeustDto
import team.waggly.backend.dto.user.SignupResponseDto
import team.waggly.backend.exception.ErrorMessage
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.SignupService
import team.waggly.backend.service.UserService
import team.waggly.backend.service.emailService.CertificationService
import team.waggly.backend.service.emailService.SendEmailService
import javax.validation.Valid

@RestController
class UserController(
    private val signupService: SignupService,
    private val sendEmailService: SendEmailService,
    private val certificationService: CertificationService,
    private val userService: UserService
) {
    @PostMapping("/user/signup")
    fun signupController(@Valid @RequestBody signupRequestDto: SignupReqeustDto, bindingResult: BindingResult): ResponseDto<Any> {
        val error = ErrorMessage(bindingResult).getError()
        if(error.isError){
            throw Exception(error.errorMsg)
        }
        signupService.userSignup(signupRequestDto)
        return ResponseDto(null, "회원가입이 완료되었습니다.", 201)
    }

    @GetMapping("/user/profile")
    fun getUserProfile(@AuthenticationPrincipal userDetailsImpl: UserDetailsImpl): ResponseDto<Any> {
        return ResponseDto(SignupResponseDto(true))
    }

    @PostMapping("/user/email")
    fun sendEmailController(@Valid @RequestBody emailCertificationRequestDto: EmailRequestDto, bindingResult: BindingResult): ResponseDto<Any> {
        val error = ErrorMessage(bindingResult).getError()
        if(error.isError){
            throw Exception(error.errorMsg)
        }
        sendEmailService.emailCertification(emailCertificationRequestDto)
        return ResponseDto(null, "이메일 전송이 완료되었습니다.", 200)
    }

    @PostMapping("/user/email/certification")
    fun certificationController(@RequestBody certificationRequestDto: CertificationRequestDto): ResponseDto<Any> {
        return ResponseDto(certificationService.certificationEmail(certificationRequestDto),"이메일 인증이 완료되었습니다.", 200)
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
    fun checkUserNickname(@Valid @RequestBody checkNicknameRequestDto: CheckNicknameRequestDto,
                          bindingResult: BindingResult
    ): ResponseDto<Any> {
        val error = ErrorMessage(bindingResult).getError()
        if(error.isError){
            throw Exception(error.errorMsg)
        }
        signupService.checkUserNickname(checkNicknameRequestDto)
        return ResponseDto(null, "닉네임 중복 확인에 성공하였습니다." , 200)
    }
}