package team.waggly.backend.model

import java.time.LocalTime
import javax.persistence.*

@Entity
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(columnDefinition = "VARCHAR(255)")
    var title: String,

    @Column(columnDefinition = "VARCHAR(255)")
    var description: String,

    @Column(columnDefinition = "VARCHAR(30)")
    val college: String, //단과대 enum

    @Column(columnDefinition = "VARCHAR(30)")
    var activeStatus: String, // enum

    @Column
    @ManyToOne
    val author: User,

    @Column
    val createdAt: LocalTime,

    @Column
    var modifiedAt: LocalTime
)