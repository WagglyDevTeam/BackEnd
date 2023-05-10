package team.waggly.backend.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.chat.MessageResponseDto
import team.waggly.backend.dto.chat.WrapMessageResponseDto
import team.waggly.backend.dto.chatroomdto.ChatRoomInfoResponseDto
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

    // pageable 메시지 목록 불러오기
    @GetMapping("/chat/room")
    fun getPageableAllChatMessage(@RequestParam roomId: Long, @RequestParam pageCount: Int, @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl?) : ResponseDto<WrapMessageResponseDto> {
        val user = userDetailsImpl?.user ?: throw Exception("회원 정보가 없습니다.")
        return ResponseDto(chatRoomService.getAllChatMessage(user, roomId, pageCount))
    }

    // 채팅방 입장시 최초 1번 불러오는 API
    @GetMapping("/chat/room/{roomid}")
    fun getChatRoomInfomation(@PathVariable roomId: Long, @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl?) : ResponseDto<ChatRoomInfoResponseDto> {
        val user = userDetailsImpl?.user ?: throw Exception("회원 정보가 없습니다.")
        return ResponseDto(chatRoomService.getChatRoomInfomation(user, roomId))
    }

    // TODO: 채팅방 설정 바인드 API 
}