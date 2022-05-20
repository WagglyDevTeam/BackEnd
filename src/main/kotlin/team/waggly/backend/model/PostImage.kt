package team.waggly.backend.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class PostImage(post: Post, imageUrl: String, originalName: String, uploadName: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @ManyToOne
    var post: Post = post

    @Column(columnDefinition = "VARCHAR(255)")
    var imageUrl: String = imageUrl

    @Column(columnDefinition = "VARCHAR(255)")
    var originalName: String = originalName

    @Column(columnDefinition = "VARCHAR(255)")
    var uploadName: String = uploadName

    @Column
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column
    var deletedAt: LocalDateTime? = null
}