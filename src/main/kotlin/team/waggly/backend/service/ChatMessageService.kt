package team.waggly.backend.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.config.redispubsub.RedisPublisher
import team.waggly.backend.dto.chat.*
import team.waggly.backend.repository.ChatRoomRepository
import team.waggly.backend.repository.UserRepository
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.security.jwt.JwtDecoder

@Service
class ChatMessageService(
        private val chatRoomRepository: ChatRoomRepository,
        private val userRepository: UserRepository,
        private val redisPublisher: RedisPublisher,
        private val jwtDecoder: JwtDecoder
) {
    fun sendChatMessage(requestDto: ChatMessageRequestDto) {
        val chatRoom = chatRoomRepository.findByIdOrNull(requestDto.roomId)
                ?: throw IllegalArgumentException("채팅방이 존재하지 않습니다.")

        var token = requestDto.token
        token = token.substring(7)
        val username = jwtDecoder.decodeUsername(token)
        val user = userRepository.findByEmail(username)

        redisPublisher.chatMessagePublish(
                ChatMessageResponseDto(
                        roomId = chatRoom.id!!,
                        sender = SenderResponseDto(user!!),
                        message = requestDto.message,
                )
        )
    }

    fun sendChatImage(requestDto: ChatImageRequestDto) {
        val chatRoom = chatRoomRepository.findByIdOrNull(requestDto.roomId)
            ?: throw IllegalArgumentException("채팅방이 존재하지 않습니다.")

        var token = requestDto.token
        token = token.substring(7)
        val username = jwtDecoder.decodeUsername(token)
        val user = userRepository.findByEmail(username)

        redisPublisher.chatImagePublish(
            ChatImageResponseDto(
                roomId = chatRoom.id!!,
                sender = SenderResponseDto(user!!),
                imageCode = requestDto.imageCode,
            )
        )
    }
}