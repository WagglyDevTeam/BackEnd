package team.waggly.backend.model

import team.waggly.backend.commomenum.ActiveStatusType
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Comment(
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

    @Column(columnDefinition = "VARCHAR(10)")
    val activeStatus: ActiveStatusType = ActiveStatusType.ACTIVE,

    @Column
    val createdAt: LocalDateTime = LocalDateTime.now(),
)