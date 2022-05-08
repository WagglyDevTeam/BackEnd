package team.waggly.backend.model

import java.time.LocalTime
import javax.persistence.*

@Entity
class ChatRoom (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(columnDefinition = "VARCHAR(30)")
    var purpose: String,

    @Column
    val createdAt: LocalTime,
)