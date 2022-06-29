package team.waggly.backend.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import team.waggly.backend.dto.myPageDto.*
import team.waggly.backend.model.User
import team.waggly.backend.repository.UserRepository
import team.waggly.backend.service.awsS3.S3Uploader
import team.waggly.backend.service.tika.TikaService
import javax.transaction.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val s3Uploader: S3Uploader,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val tikaService: TikaService,
) {
    @Transactional
    fun updateUserProfile(user: User, updateUserProfileRequestDto: UpdateUserProfileRequestDto): UpdateUserProfileDto {
        val updateUser = userRepository.findByIdOrNull(user.id!!)
                ?: throw java.lang.IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        if (!tikaService.typeCheck(updateUserProfileRequestDto.profileImg)) {
            throw IllegalArgumentException("올바른 파일 형식이 아닙니다. (.jpg, .jpeg, .gif, .png)")
        }

        updateUser.nickName = updateUserProfileRequestDto.nickname
        updateUser.profileImgUrl = s3Uploader.upload(updateUserProfileRequestDto.profileImg, tikaService.mimeType(updateUserProfileRequestDto.profileImg))

        return UpdateUserProfileDto(
                updateUser.profileImgUrl,
                updateUser.nickName
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
}