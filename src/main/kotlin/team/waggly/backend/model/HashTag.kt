package team.waggly.backend.model

import javax.persistence.*

@Entity
class HashTag (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(columnDefinition = "VARCHAR(20)")
    val hashTagName: String,
)