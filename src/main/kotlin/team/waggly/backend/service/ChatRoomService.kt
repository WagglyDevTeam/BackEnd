package team.waggly.backend.service

import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ActiveStatusType
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
        val myChatRoomInfoList: List<ChatParticipantsInfo> = chatParticipantsInfoRepository.findAllByUserAndActiveStatus(user, ActiveStatusType.ACTIVE)

        val chatRoomResponseDtoList: MutableList<ChatRoomResponseDto> = arrayListOf()

        for (myChatRoomInfo in myChatRoomInfoList) {
            val chatRoomResponseDto = ChatRoomResponseDto(myChatRoomInfo)
            chatRoomResponseDtoList.add(chatRoomResponseDto)
        }
        return chatRoomResponseDtoList
    }
}
