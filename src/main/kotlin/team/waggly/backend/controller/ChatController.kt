package team.waggly.backend.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.chat.ChatImageRequestDto
import team.waggly.backend.dto.chat.ChatMessageRequestDto
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.ChatMessageService

@RestController
class ChatController(
        private val chatMessageService: ChatMessageService,
) {
    @MessageMapping("/chat/message")
    fun chatMessage(requestDto: ChatMessageRequestDto) {
        chatMessageService.sendChatMessage(requestDto)
    }

    @PostMapping("/chat/image")
    fun chatImage(
        @ModelAttribute requestDto: ChatImageRequestDto,
        @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl
    ) : ResponseDto<Any> {
        return chatMessageService.sendChatImage(requestDto, userDetailsImpl.user)
    }
}