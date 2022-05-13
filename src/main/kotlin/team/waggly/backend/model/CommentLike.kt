package team.waggly.backend.model

import javax.persistence.*

@Entity
class CommentLike (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    @ManyToOne
    val comment: Comment,

    @Column(columnDefinition = "INTEGER(10)")
    val userId: Long,
)