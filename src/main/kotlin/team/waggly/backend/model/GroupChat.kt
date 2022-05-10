package team.waggly.backend.model

import javax.persistence.*

@Entity
class GroupChat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val Id: Long? = null,

    @Column
    @ManyToOne
    val host: User,

    @Column
    @ManyToOne
    val university: University,

    @Column(columnDefinition = "VARCHAR(100)")
    val title: String,

    @Column(columnDefinition = "VARCHAR(30)")
    val category: CategoryType, // 이넘?

    @Column(columnDefinition = "INTEGER(10)")
    val participantLimit: Int,

    @Column(columnDefinition = "VARCHAR(255)")
    val guide: String, // 방 룰?
) {
    enum class CategoryType {
        CLUB,   //동아리
        STUDY,  // 스터디
        SCHOOL, //학업
        EMPLOYMENT, // 취업
        LIFE,   //생활
        FREE,   //자유
        ACTIVITY //대외활동
    }
}