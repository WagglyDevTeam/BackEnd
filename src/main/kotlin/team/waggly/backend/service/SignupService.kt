package team.waggly.backend.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.user.CheckNicknameRequestDto
import team.waggly.backend.dto.user.SignupReqeustDto
import team.waggly.backend.dto.user.SignupResponseDto
import team.waggly.backend.model.User
import team.waggly.backend.repository.MajorRepository
import team.waggly.backend.repository.UserRepository

@Service
class SignupService(
    private val userRepository: UserRepository,
    private val majorRespository: MajorRepository
    ){
    private val passwordEncoder = BCryptPasswordEncoder()

    fun userSignup(signupRequestDto: SignupReqeustDto): ResponseDto<Any> {
        val initProfileImg = "기본이미지 Url"
        val findUser = userRepository.findByEmail(signupRequestDto.email)

        if(findUser != null){
            return ResponseDto(null, "이메일당 하나의 아이디만 생성가능합니다.", 404)
        }

        val major = majorRespository.findByIdOrNull(signupRequestDto.major) ?: return ResponseDto(null, "해당되는 학과가 없습니다.", 404)
        val gender = when (signupRequestDto.gender) {
            "male" -> User.GenderType.MALE
            "female" -> User.GenderType.FEMALE
            else -> return ResponseDto(null, "성별 데이터가 잘못되었습니다.", 404)
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
            major,
            null,
            User.AuthType.USER,
            null
        )

        userRepository.save(newUser)
        return ResponseDto()
    }

    fun checkUserNickname(checkNicknameRequestDto: CheckNicknameRequestDto) {
        userRepository.findByNickName(checkNicknameRequestDto.nickname)
    }

}