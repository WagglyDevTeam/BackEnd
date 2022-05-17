package team.waggly.backend.model

import javax.persistence.*

@Entity
class UserSetting (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    val user: User,

    @Column
    var allowHelpRequest: Boolean = true,

    @Column
    var allowFriendRequest: Boolean = true,

    @Column
    var allowPushAlarm: Boolean = true, // 전체 푸시 알림

    @Column
    var allowMarketingAlarm: Boolean = false, // 마케팅 알림
)