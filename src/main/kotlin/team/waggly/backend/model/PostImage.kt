package team.waggly.backend.model

import javax.persistence.*

@Entity
class PostImage(post: Post, imageUrl: String, originalName: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @ManyToOne
    var post: Post = post

    @Column(columnDefinition = "VARCHAR(255)")
    var imageUrl: String = imageUrl

    @Column(columnDefinition = "VARCHAR(255)")
    var originalName: String = originalName
}