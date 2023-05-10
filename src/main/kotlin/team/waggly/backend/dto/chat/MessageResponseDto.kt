package team.waggly.backend.dto.chat

import team.waggly.backend.model.mongo.Message
import java.time.LocalDateTime

data class WrapMessageResponseDto(
        var messageLit: List<MessageResponseDto>
)
data class MessageResponseDto (
        var roomId: Long?,
        var senderId: Long?,
        var body: String?,
        var type: String?,
        var createAt: LocalDateTime?
){
    constructor(message: Message) : this(
            message.roomId,
            message.senderId,
            message.body,
            message.type,
            message.createdAt
    )
}