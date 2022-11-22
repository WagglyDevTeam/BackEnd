package team.waggly.backend.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Service
import team.waggly.backend.dto.chatmessagedto.ChatMessageResponseDto


@Service
class RedisPublisherService(
        private val redisTemplate: RedisTemplate<Any, Any>,
        private val channelTopic: ChannelTopic,
) {
    fun publish(messageDto: ChatMessageResponseDto) {
        redisTemplate.convertAndSend(channelTopic.topic, messageDto)
    }
}