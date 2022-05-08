package team.waggly.backend.model

import java.time.LocalTime
import javax.persistence.*

@Entity
class Comment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    @ManyToOne
    val post: Post,

    @Column
    @ManyToOne
    val reply: Comment? = null,

    @Column
    @ManyToOne
    val user: User,

    @Column(columnDefinition = "VARCHAR(255)")
    val description: String,

    @Column(columnDefinition = "VARCHAR(15)")
    val activeStatus: String,

    @Column
    val createdAt: LocalTime,
)