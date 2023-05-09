package team.waggly.backend.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.config.redispubsub.RedisPublisher
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.chat.*
import team.waggly.backend.model.User
import team.waggly.backend.model.mongo.Message
import team.waggly.backend.repository.ChatRoomRepository
import team.waggly.backend.repository.UserRepository
import team.waggly.backend.repository.mongo.MessageRepository
import team.waggly.backend.security.jwt.JwtDecoder
import team.waggly.backend.service.awsS3.S3Uploader

@Service
class ChatMessageService(
    private val chatRoomRepository: ChatRoomRepository,
    private val userRepository: UserRepository,
    private val redisPublisher: RedisPublisher,
    private val messageRepository: MessageRepository,
    private val s3Uploader: S3Uploader,
    private val jwtDecoder: JwtDecoder
) {
    fun sendChatMessage(requestDto: ChatMessageRequestDto) {
        val chatRoom = chatRoomRepository.findByIdOrNull(requestDto.roomId)
                ?: throw IllegalArgumentException("채팅방이 존재하지 않습니다.")

        var token = requestDto.token
        token = token.substring(7)
        val username = jwtDecoder.decodeUsername(token)
        val user = userRepository.findByEmail(username)
        val mongoMessage = Message(roomId = chatRoom.id!!, sender = user?.id, body = requestDto.message)

        println(mongoMessage.body)
        messageRepository.save(mongoMessage)

        redisPublisher.chatMessagePublish(
                ChatMessageResponseDto(
                        roomId = chatRoom.id!!,
                        sender = SenderResponseDto(user!!),
                        message = requestDto.message,
                )
        )
    }

    fun sendChatImage(requestDto: ChatImageRequestDto, user: User) : ResponseDto<Any> {
        val chatRoom = chatRoomRepository.findByIdOrNull(requestDto.roomId)
            ?: throw IllegalArgumentException("채팅방이 존재하지 않습니다.")

        val file = requestDto.image
        val fileUrl = s3Uploader.upload(file)

        redisPublisher.chatImagePublish(
            ChatImageResponseDto(
                roomId = chatRoom.id!!,
                sender = SenderResponseDto(user!!),
                imageUrl = fileUrl,
            )
        )

        return ResponseDto()
    }
}