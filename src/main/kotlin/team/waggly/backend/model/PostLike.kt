package team.waggly.backend.model

import team.waggly.backend.commomenum.ActiveStatusType
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

    @Column(columnDefinition = "varchar(10)")
    @Enumerated(EnumType.STRING)
    var status: ActiveStatusType = ActiveStatusType.ACTIVE
}