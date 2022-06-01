package team.waggly.backend.model

import team.waggly.backend.commomenum.ActiveStatusType
import javax.persistence.*

@Entity
class CommentLike (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    val comment: Comment,

    @Column(columnDefinition = "INTEGER(10)")
    val userId: Long,

    @Column(columnDefinition = "VARCHAR(10)")
    var activeStatus: ActiveStatusType = ActiveStatusType.ACTIVE,
)