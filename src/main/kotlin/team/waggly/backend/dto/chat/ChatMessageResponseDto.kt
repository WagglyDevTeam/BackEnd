package team.waggly.backend.dto.chat

class ChatMessageResponseDto(
        val roomId: Long,
        val sender: SenderResponseDto,
        val message: String,
) {
}