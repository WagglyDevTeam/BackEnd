package team.waggly.backend.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.email.EmailRequestDto
import team.waggly.backend.dto.myPageDto.*
import team.waggly.backend.dto.user.GetDeviceTokenRequestDto
import team.waggly.backend.dto.user.PutDeviceTokenRequestDto
import team.waggly.backend.model.User
import team.waggly.backend.repository.UserRepository
import team.waggly.backend.service.awsS3.S3Uploader
import javax.transaction.Transactional

@Service
class UserService(
        private val userRepository: UserRepository,
        private val s3Uploader: S3Uploader,
        private val passwordEncoder: BCryptPasswordEncoder,
) {
    @Transactional
    fun updateUserProfile(user: User, updateUserProfileRequestDto: UpdateUserProfileRequestDto): UpdateUserProfileDto {
        val updateUser = userRepository.findByIdOrNull(user.id!!)
                ?: throw java.lang.IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        updateUser.nickName = updateUserProfileRequestDto.nickname

        return UpdateUserProfileDto(
                updateUser.nickName
        )
    }

    @Transactional
    fun updateUserProfileImg(user: User, updateUserProfileImgRequestDto: UpdateUserProfileImgRequestDto): UpdateUserProfileImgDto {
        val updateUser = userRepository.findByIdOrNull(user.id!!)
            ?: throw java.lang.IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        print(updateUserProfileImgRequestDto.profileImg)
        updateUser.profileImgUrl = s3Uploader.upload(updateUserProfileImgRequestDto.profileImg)

        return UpdateUserProfileImgDto(
            updateUser.profileImgUrl,
        )
    }

    @Transactional
    fun updateUserIntroduction(user: User, updateUserIntroductionRequestDto: UpdateUserIntroductionRequestDto): UpdateUserIntroductionDto {
        val updateUser = userRepository.findByIdOrNull(user.id!!)
                ?: throw java.lang.IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        updateUser.introduction = updateUserIntroductionRequestDto.userIntroduction

        return UpdateUserIntroductionDto(
                updateUser.introduction!!
        )
    }

    fun checkPassword(user: User, checkPasswordRequestDto: PasswordRequestDto) {
        val dbUser = userRepository.findByIdOrNull(user.id!!)
                ?: throw java.lang.IllegalArgumentException("해당 유저가 존재하지 않습니다.")

        if (!passwordEncoder.matches(checkPasswordRequestDto.password, dbUser.password)) {
            throw BadCredentialsException("Invalid password")
        }
    }

    @Transactional
    fun updatePassword(user: User, updatePasswordRequestDto: PasswordRequestDto) {
        val dbUser = userRepository.findByIdOrNull(user.id!!)
                ?: throw java.lang.IllegalArgumentException("해당 유저가 존재하지 않습니다.")

        dbUser.password = passwordEncoder.encode(updatePasswordRequestDto.password)
    }

    fun existUserByEmail(emailRequestDto: EmailRequestDto): ResponseDto<Any> {
        if (userRepository.existsByEmail(emailRequestDto.email)) {
            return ResponseDto()
        } else {
            throw java.lang.IllegalArgumentException("해당 이메일이 존재하지 않습니다.")
        }
    }

    fun getDeviceToken(userId: Long): String? {
        return userRepository.findByIdOrNull(userId)?.deviceToken
    }

    @Transactional
    fun updateDeviceToken(user: User, putDeviceTokenRequestDto: PutDeviceTokenRequestDto) {
        val updateUser = userRepository.findByIdOrNull(user.id!!)
            ?: throw java.lang.IllegalArgumentException("해당 유저가 존재하지 않습니다.")

        // 이미 해당 Device Token을 사용하고 있는 유저가 있다면, 기존 Device Token을 제거
        val duplicateDeviceTokenUser = userRepository.findByDeviceToken(putDeviceTokenRequestDto.deviceToken)
        if (duplicateDeviceTokenUser != null) {
            duplicateDeviceTokenUser.deviceToken = null
        }
        // Device Token 업데이트
        updateUser.deviceToken = putDeviceTokenRequestDto.deviceToken
    }
}