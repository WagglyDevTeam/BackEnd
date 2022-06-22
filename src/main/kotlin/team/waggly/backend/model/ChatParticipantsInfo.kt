package team.waggly.backend.model

import team.waggly.backend.commomenum.ActiveStatusType
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class ChatParticipantsInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    val user: User,

    @ManyToOne
    val opponent: User,

    @ManyToOne
    val chatRoom: ChatRoom,

    @Column
    val isAlarmOn: Boolean = true,

    @Column(columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    val activeStatus: ActiveStatusType = ActiveStatusType.ACTIVE, // 이넘

    @Column
    val exitedAt: LocalDateTime? = null,

    @Column
    val joinedAt: LocalDateTime = LocalDateTime.now()
)