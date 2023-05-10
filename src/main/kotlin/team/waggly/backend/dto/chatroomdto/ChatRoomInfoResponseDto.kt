package team.waggly.backend.dto.chatroomdto

import team.waggly.backend.dto.chat.MessageResponseDto
import team.waggly.backend.dto.chat.SenderResponseDto

data class ChatRoomInfoResponseDto(
        val imageList: List<ChatRoomImageDto>,
        val messageList: List<MessageResponseDto>,
)
