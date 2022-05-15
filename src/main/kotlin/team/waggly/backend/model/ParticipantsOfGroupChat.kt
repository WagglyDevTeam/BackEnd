package team.waggly.backend.model

import team.waggly.backend.commomenum.ActiveStatusType
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class ParticipantsOfGroupChat(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    val groupChat: GroupChat,

    @Column
    val userId: Long, //연관 확인 필요

    @Column
    var isAlarmOn: Boolean = true,

    @Column(unique = true, columnDefinition = "VARCHAR(15)")
    var activeStatus: ActiveStatusType = ActiveStatusType.ACTIVE,

    @Column
    val exitedAt: LocalDateTime? = null,

    @Column
    val joinedAt: LocalDateTime = LocalDateTime.now()
    )