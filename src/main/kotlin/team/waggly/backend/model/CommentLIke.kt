package team.waggly.backend.model

import java.time.LocalTime
import javax.persistence.*

@Entity
class CommentLIke (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    val comment: Comment,

    @Column(columnDefinition = "INTEGER(10)")
    val userId: Long,
)