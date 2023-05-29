package team.waggly.backend.model

import javax.persistence.*

@Entity
class ChatTopic(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val chatTopicId: Long? = null,

    @Column
    @Enumerated(EnumType.STRING)
    val interestTopic: InterestTopic,

    @Column
    val userId: Long,
) {
    enum class InterestTopic {
        EMPOLYMENT,
        SCHOOL,
        STUDY,
        ETC
    }
}