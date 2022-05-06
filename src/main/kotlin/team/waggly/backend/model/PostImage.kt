package team.waggly.backend.model

import javax.persistence.*

@Entity
class PostImage (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column
    val imageUrl: String,
    @ManyToOne
    val post: Post
    )