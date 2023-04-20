package team.waggly.backend.model.mongo

import org.springframework.data.mongodb.core.mapping.Document
import team.waggly.backend.dto.chat.SenderResponseDto
import javax.persistence.Id

@Document(collation = "message")
class Message(
        @Id
        var id : String ? = null,
        var sender : SenderResponseDto,
        var message : String
)