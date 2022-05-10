package team.waggly.backend.model

import team.waggly.backend.commomenum.ActiveStatusType
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class ChatParticipantsInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    @ManyToOne
    val user: User,

    @Column
    @ManyToOne
    val opponent: User,

    @Column
    @ManyToOne
    val chatRoom: ChatRoom,

    @Column
    val isAlarmOn: Boolean = true,

    @Column(columnDefinition = "VARCHAR(10)")
    val activeStatus: ActiveStatusType = ActiveStatusType.ACTIVE, // 이넘

    @Column
    val exitedAt: LocalDateTime? = null,

    @Column
    val joinedAt: LocalDateTime = LocalDateTime.now()
)