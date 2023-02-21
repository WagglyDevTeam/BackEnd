package team.waggly.backend.config.redispubsub

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Service
import team.waggly.backend.dto.chat.ChatMessageResponseDto

@Service
class RedisPublisher(
        private val redisTemplate: RedisTemplate<Any, Any>,
        private val channelTopic: ChannelTopic
) {
    fun publish(messageDto: ChatMessageResponseDto) {
        redisTemplate.convertAndSend(channelTopic.topic, messageDto)
    }
}