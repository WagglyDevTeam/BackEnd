package team.waggly.backend.dto.chat

class ChatMessageRequestDto(
        val roomId: Long,
        var sender: String,
        val message: String,
) {
}