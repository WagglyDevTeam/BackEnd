package team.waggly.backend.model

import javax.persistence.*

@Entity
class BannedUserList (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    @ManyToOne
    val groupChat: GroupChat,

    @Column(columnDefinition = "INTEGER(10)")
    val userId: Int
)