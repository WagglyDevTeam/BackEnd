package team.waggly.backend.model.mongo

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Document(collection = "message")
class Message(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: String? = null,

        @Field(name = "room_id")
        var roomId: Long?,

        @Field(name = "sender_id")
        var senderId: Long?,

        @Field(name = "body")
        var body: String?,

        @Field(name = "type")
        var type: String?,

        @Field(name = "create_at")
        var createdAt: LocalDateTime = LocalDateTime.now()
)