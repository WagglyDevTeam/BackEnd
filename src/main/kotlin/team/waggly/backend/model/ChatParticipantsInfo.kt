package team.waggly.backend.model

import java.time.LocalTime
import javax.persistence.*

@Entity
class ChatParticipantsInfo (
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
    val isAlarmOn: Boolean,

    @Column(columnDefinition = "VARCHAR(30)")
    val activeStatus: String, // 이넘

    @Column
    val exitedAt: LocalTime,

    @Column
    val joinedAt: LocalTime,
)