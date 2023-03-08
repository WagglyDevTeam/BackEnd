package team.waggly.backend.config.redispubsub

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import team.waggly.backend.dto.chat.ChatMessageResponseDto

@Service
class RedisPublisher(
        private val redisTemplate: RedisTemplate<String, Any>,
        private val channelTopic: ChannelTopic,
        private val messagingTemplate: SimpMessageSendingOperations
) {
    fun publish(messageDto: ChatMessageResponseDto) {
        messagingTemplate.convertAndSend("/sub/chat/room/1", messageDto)
//        redisTemplate.convertAndSend("/sub/chat/room/1", messageDto)
    }
}