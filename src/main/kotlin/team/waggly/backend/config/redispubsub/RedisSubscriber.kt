package team.waggly.backend.config.redispubsub

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import team.waggly.backend.dto.chat.ChatMessageResponseDto

@Service
class RedisSubscriber(
        private val messagingTemplate: SimpMessageSendingOperations
) {
    fun sendMessage(publishedMessage: String) {
        try {
            val objectMapper = ObjectMapper()
            val responseDto = objectMapper.readValue(publishedMessage, ChatMessageResponseDto::class.java)
            messagingTemplate.convertAndSend("/sub/chat/room/" + responseDto.roomId, responseDto)
        } catch (e: Exception) {
            // log
        }
    }
}