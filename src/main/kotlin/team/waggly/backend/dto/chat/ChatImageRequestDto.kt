package team.waggly.backend.dto.chat

import org.springframework.web.multipart.MultipartFile

data class ChatImageRequestDto(
    val roomId: Long,
    val image: MultipartFile,
)
