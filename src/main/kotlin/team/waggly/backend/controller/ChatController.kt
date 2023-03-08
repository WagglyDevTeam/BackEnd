package team.waggly.backend.controller

import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestHeader
import team.waggly.backend.dto.chat.ChatMessageRequestDto
import team.waggly.backend.security.jwt.JwtDecoder
import team.waggly.backend.service.ChatMessageService

@Controller
class ChatController(
        private val jwtDecoder: JwtDecoder,
        private val chatMessageService: ChatMessageService,
) {
    @MessageMapping("/chat/message")
    fun chatMessage(requestDto: ChatMessageRequestDto
    ) {
        print(requestDto.message)
//        var token = token
//        print(token)
//        print(token)
//        print(token)
//        print(token)
//        token = token.substring(7)
        requestDto.sender = requestDto.sender
        chatMessageService.sendChatMessage(requestDto)
    }
}