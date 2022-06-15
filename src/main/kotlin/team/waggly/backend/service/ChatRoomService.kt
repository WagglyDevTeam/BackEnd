package team.waggly.backend.service

import org.springframework.stereotype.Service
import team.waggly.backend.dto.chatroomdto.ChatRoomResponseDto
import team.waggly.backend.model.ChatParticipantsInfo
import team.waggly.backend.model.User
import team.waggly.backend.repository.ChatParticipantsInfoRepository
import team.waggly.backend.repository.ChatRoomRepository

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatParticipantsInfoRepository: ChatParticipantsInfoRepository
) {
    fun getAllChatRoom(user: User): List<ChatRoomResponseDto> {
//        TODO(
//            "1. chatParticipantsInfo 에서 user = user 인 채팅방 정보 찾아옴" +
//                    "2. ChatRoomResponseDto 리스트 생성" +
//                    "3. 찾아온 채팅방 정보에서 ChatRoomResponseDto 생성 후 DTO 리스트에 추가 "
//        )
// 생성한 채팅방이 없을 경우 예외가 아니라 다른 처리가 필요할듯
        val myChatRoomInfoList: List<ChatParticipantsInfo> =
            chatParticipantsInfoRepository.findAllByUser(user) ?: throw IllegalArgumentException("생성된 채팅방이 없습니다.")

        val chatRoomResponseDtoList: MutableList<ChatRoomResponseDto> = arrayListOf()

        for (myChatRoomInfo in myChatRoomInfoList) {
            val chatRoomResponseDto = ChatRoomResponseDto(myChatRoomInfo)
            chatRoomResponseDtoList.add(chatRoomResponseDto)
        }
        return chatRoomResponseDtoList
    }
}
