package team.waggly.backend.controller

import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestHeader
import team.waggly.backend.dto.chat.ChatMessageRequestDto
import team.waggly.backend.security.UserDetailsImpl
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
}