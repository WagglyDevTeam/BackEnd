package team.waggly.backend.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.config.redispubsub.RedisPublisher
import team.waggly.backend.dto.chat.ChatMessageRequestDto
import team.waggly.backend.dto.chat.ChatMessageResponseDto
import team.waggly.backend.repository.ChatRoomRepository

@Service
class ChatMessageService(
        private val chatRoomRepository: ChatRoomRepository,
        private val redisPublisher: RedisPublisher,
) {
    fun sendChatMessage(requestDto: ChatMessageRequestDto) {
        val chatRoom = chatRoomRepository.findByIdOrNull(requestDto.roomId)
                ?: throw IllegalArgumentException("채팅방이 존재하지 않습니다.")

        redisPublisher.publish(
                ChatMessageResponseDto(
                        roomId = chatRoom.id!!,
                        message = requestDto.message,
                )
        )
    }
}