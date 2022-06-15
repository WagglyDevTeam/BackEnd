package team.waggly.backend.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.chatroomdto.ChatRoomResponseDto
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.ChatRoomService

@RestController
class ChatRoomController(private val chatRoomService: ChatRoomService) {

    @GetMapping("/chat/myList")
    fun getAllChatRoom(@AuthenticationPrincipal userDetailsImpl: UserDetailsImpl?): ResponseDto<List<ChatRoomResponseDto>> {
        val user = userDetailsImpl?.user ?: throw Exception("회원정보가 없습니다.")
        return ResponseDto(chatRoomService.getAllChatRoom(user))
    }
}