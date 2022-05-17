package team.waggly.backend.model

import javax.persistence.*

@Entity
class PostImage (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    val post: Post,

    @Column(columnDefinition = "VARCHAR(255)")
    val imageUrl: String,
)