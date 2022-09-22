package team.waggly.backend.dto.myPageDto

import org.springframework.web.multipart.MultipartFile

class UpdateUserProfileRequestDto(
        val nickname: String,
        val profileImg: MultipartFile
)

class UpdateUserProfileImgRequestDto(
        val profileImg: MultipartFile
)