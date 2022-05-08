package team.waggly.backend.model

import javax.persistence.*

@Entity
class Alarm (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(columnDefinition = "VARCHAR(30)")
    val alarmType: String,

    @Column(columnDefinition = "INTEGER(10)")
    val targetId: Int,

    @Column(columnDefinition = "INTEGER(10)")
    val receiverId: Int,
)