package team.waggly.backend.dto.mypagedto

import org.springframework.web.multipart.MultipartFile

class UpdateUserProfileRequestDto(
        val nickname: String,
        val profileImg: MultipartFile
)