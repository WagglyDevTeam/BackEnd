package team.waggly.backend.model

import javax.persistence.*

@Entity
class PostLike (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    val post: Post,

    @Column(columnDefinition = "INTEGER(10)")
    val userId: Long
)