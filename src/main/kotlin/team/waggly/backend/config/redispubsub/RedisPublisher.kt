package team.waggly.backend.config.redispubsub

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import team.waggly.backend.dto.chat.ChatImageResponseDto
import team.waggly.backend.dto.chat.ChatMessageResponseDto

@Service
class RedisPublisher(
        private val redisTemplate: RedisTemplate<String, Any>,
        private val channelTopic: ChannelTopic,
        private val messagingTemplate: SimpMessageSendingOperations
) {
    // 일반 메시지 보내기
    fun chatMessagePublish(messageDto: ChatMessageResponseDto) {
        messagingTemplate.convertAndSend("/sub/chat/message/room/" + messageDto.roomId, messageDto)
    }

    // 사진 메시지 보내기
    fun chatImagePublish(messageDto : ChatImageResponseDto){
        messagingTemplate.convertAndSend("/sub/chat/image/room/" + messageDto.roomId, messageDto)
    }
}