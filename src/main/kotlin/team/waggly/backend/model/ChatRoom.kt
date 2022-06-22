package team.waggly.backend.model

import team.waggly.backend.commomenum.ChatPurposeType
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class ChatRoom (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    var chatPurpose: ChatPurposeType,

    @Column
    val createdAt: LocalDateTime = LocalDateTime.now(),
)