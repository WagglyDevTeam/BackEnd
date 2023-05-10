package team.waggly.backend.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.dto.chat.ChatImageResponseDto
import team.waggly.backend.dto.chat.MessageResponseDto
import team.waggly.backend.dto.chat.WrapMessageResponseDto
import team.waggly.backend.dto.chatroomdto.ChatRoomImageDto
import team.waggly.backend.dto.chatroomdto.ChatRoomInfoResponseDto
import team.waggly.backend.dto.chatroomdto.ChatRoomResponseDto
import team.waggly.backend.model.ChatParticipantsInfo
import team.waggly.backend.model.User
import team.waggly.backend.model.mongo.Message
import team.waggly.backend.repository.ChatParticipantsInfoRepository
import team.waggly.backend.repository.ChatRoomRepository
import team.waggly.backend.repository.mongo.MessageRepository


@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
    private val messageRepository: MessageRepository,
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

    fun getAllChatMessage(user: User, roomId: Long, pageCount: Int): WrapMessageResponseDto {
        return WrapMessageResponseDto(getMessageList(user, roomId, pageCount))
    }

    fun getChatRoomInfomation(user: User, roomId: Long) : ChatRoomInfoResponseDto {

        // 1. 최근 20개 메시지 불러오기
        val messageList: List<MessageResponseDto> = getMessageList(user, roomId, 0)

        // 2. 이미지 불러오기
        val findImageMessageList: List<Message> = messageRepository.findAllByType("image")
        val imageList: MutableList<ChatRoomImageDto> = arrayListOf()

        for(message in findImageMessageList){
            imageList.add(ChatRoomImageDto(message))
        }

        return ChatRoomInfoResponseDto(imageList, messageList)
    }

    fun getMessageList(user: User, roomId: Long, pageCount: Int) : List<MessageResponseDto> {
        val page: Page<Message> = messageRepository.findAllByRoomId(roomId,
                PageRequest.of(pageCount, 20, Sort.Direction.ASC, "create_at"))
        val myChatMessageList: List<Message> = page.content
        val chatMessageResponseDtoList: MutableList<MessageResponseDto> = arrayListOf()

        for(myChatMessage in myChatMessageList){
            val dto = MessageResponseDto(myChatMessage)
            chatMessageResponseDtoList.add(dto)
        }

        return chatMessageResponseDtoList
    }
}
