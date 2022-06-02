package team.waggly.backend.model

import team.waggly.backend.commomenum.ActiveStatusType
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    val post: Post,

    @ManyToOne
    val parentComment: Comment? = null,

    @ManyToOne
    val user: User,

    @Column(columnDefinition = "VARCHAR(255)")
    val description: String,

    @Column
    var isAnonymous: Boolean = false,

    @Column(columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    val activeStatus: ActiveStatusType = ActiveStatusType.ACTIVE,

    @Column
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column
    var deletedAt: LocalDateTime? = null
)