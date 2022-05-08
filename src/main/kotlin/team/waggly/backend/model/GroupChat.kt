package team.waggly.backend.model

import javax.persistence.*

@Entity
class GroupChat (
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
    val category: String, // 이넘?

    @Column(columnDefinition = "INTEGER(10)")
    val participantLimit: Int,

    @Column(columnDefinition = "VARCHAR(255)")
    val guide: String, // 방 룰?
)