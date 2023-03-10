package team.waggly.backend.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import team.waggly.backend.dto.chat.ChatImageRequestDto
import team.waggly.backend.dto.chat.ChatMessageRequestDto
import team.waggly.backend.security.jwt.JwtDecoder
import team.waggly.backend.service.ChatMessageService

@Controller
class ChatController(
        private val jwtDecoder: JwtDecoder,
        private val chatMessageService: ChatMessageService,
) {
    @MessageMapping("/chat/message")
    fun chatMessage(requestDto: ChatMessageRequestDto) {
        chatMessageService.sendChatMessage(requestDto)
    }

    @MessageMapping("/chat/image")
    fun chatImage(requestDto: ChatImageRequestDto){
        chatMessageService.sendChatImage(requestDto)
    }
}