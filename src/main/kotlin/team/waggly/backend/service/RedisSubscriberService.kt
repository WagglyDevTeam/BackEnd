package team.waggly.backend.service

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import team.waggly.backend.dto.chatmessagedto.ChatMessageResponseDto

@Service
class RedisSubscriberService(
        private val messagingTemplate: StringRedisTemplate,
) {
    private val log = KotlinLogging.logger {}

    fun sendMessage(publishedMessage: String) {
        try {
            val objectMapper = ObjectMapper()
            val responseDto = objectMapper.readValue(publishedMessage, ChatMessageResponseDto::class.java)
            messagingTemplate.convertAndSend("/sub/chat/room/" + responseDto.roomId, responseDto)
            messagingTemplate.convertAndSend("/sub/alarm/" + responseDto.receiverId, responseDto)
        } catch (e: Exception) {
            log.error(e.message)
        }
    }
}