package team.waggly.backend.dto.chat

data class ChatRoomInfoResponseDto(
        val roomId: Long,
        val sender: SenderResponseDto,
        val message: String
)
