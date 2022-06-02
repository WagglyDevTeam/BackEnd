package team.waggly.backend.service

import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import team.waggly.backend.dto.SignupDto
import team.waggly.backend.model.User
import team.waggly.backend.repository.UserRepository

@Service
class SignupService(private val userRepository: UserRepository){
    private val passwordEncoder = BCryptPasswordEncoder()

    fun userSignup(signupRequestDto: SignupDto.Request): SignupDto.Response {
        val initProfileImg = "기본이미지 Url"
        val gender = when (signupRequestDto.gender) {
            "male" -> User.GenderType.MALE
            "female" -> User.GenderType.FEMALE
            else -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"성별데이터가 잘못되었습니다.")
        }
        // 스프링 시큐리티에서 passwordEncoder로 인코딩후 넣어준다.
        val encodedPassword = passwordEncoder.encode(signupRequestDto.password)
        print(User.GenderType.FEMALE)
        print(gender)
        val newUser = User(
            null,
            signupRequestDto.email,
            initProfileImg,
            signupRequestDto.nickname,
            encodedPassword,
            gender,
            signupRequestDto.classNumber,
            User.UserActiveStatusType.ACTIVE,
            null,
            null,
            User.AuthType.USER,
            null
        )

        userRepository.save(newUser)
        return SignupDto.Response(true)
    }

}