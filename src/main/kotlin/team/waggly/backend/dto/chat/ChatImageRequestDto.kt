package team.waggly.backend.dto.chat

data class ChatImageRequestDto(
    val roomId: Long,
    var token: String,
    val imageCode: String,
)
