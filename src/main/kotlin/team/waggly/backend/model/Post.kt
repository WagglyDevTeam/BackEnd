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
    @Column
    var description: String,
    @Column
    val college: String, //단과대 enum
    @Column
    var activeStatus: String, // enum
    @ManyToOne
    val author: User,
    @Column
    val createdAt: LocalTime,
    @Column
    var modifiedAt: LocalTime
)