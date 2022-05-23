package team.waggly.backend.model

import javax.persistence.*

@Entity(name = "post_like")
class PostLike(post: Post, userId: Long){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @ManyToOne
    var post: Post = post

    @Column(columnDefinition = "INTEGER(10)")
    var userId: Long = userId
}