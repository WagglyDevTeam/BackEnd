package team.waggly.backend.dto.chatroomdto

import team.waggly.backend.model.ChatParticipantsInfo
import team.waggly.backend.model.mongo.Message
import java.time.LocalDateTime

data class ChatRoomImageDto(
        var senderId: Long,
        var imageUrl: String,
        var createdAt: LocalDateTime
){
    constructor(message: Message): this(
            senderId = message.senderId!!,
            imageUrl = message.body!!,
            createdAt = message.createdAt!!
    )
}
