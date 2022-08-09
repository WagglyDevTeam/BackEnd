package team.waggly.backend.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class HashTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    val post: Post,

    @Column(columnDefinition = "VARCHAR(20)") //제한필요
    val hashTagName: String,

    @Column
    var deletedAt: LocalDateTime? = null,
)