package team.waggly.backend.dto.chat

class ChatMessageRequestDto(
        val roomId: Long,
        var token: String,
        val message: String,
) {
}