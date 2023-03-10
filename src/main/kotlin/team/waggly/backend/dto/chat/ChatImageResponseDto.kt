package team.waggly.backend.dto.chat

data class ChatImageResponseDto(
    val roomId: Long,
    val sender: SenderResponseDto,
    val imageCode: String,
)
