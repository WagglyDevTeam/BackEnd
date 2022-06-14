package team.waggly.backend.service

import org.springframework.stereotype.Service
import team.waggly.backend.dto.chatroomdto.ChatRoomResponseDto
import team.waggly.backend.model.User
import team.waggly.backend.repository.ChatRoomRepository

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository
) {
    fun getAllChatRoom(user: User): List<ChatRoomResponseDto> {
        TODO(
            "1. chatParticipantsInfo 에서 user = user 인 채팅방 정보 찾아옴" +
                    "2. ChatRoomResponseDto 리스트 생성" +
                    "3. 찾아온 채팅방 정보에서 ChatRoomResponseDto 생성 후 리스트에 추가 "
        )
    }
}
