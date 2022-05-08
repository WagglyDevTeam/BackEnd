package team.waggly.backend.model

import javax.persistence.*

@Entity
class UserSetting (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    @OneToOne
    val user: User,

    @Column
    var allowHelpRequest: Boolean,

    @Column
    var allowFriendRequest: Boolean,

    @Column
    var allowPushAlarm: Boolean, // 전체 푸시 알림

    @Column
    var allowMarketingAlarm: Boolean, // 마케팅 알림
)