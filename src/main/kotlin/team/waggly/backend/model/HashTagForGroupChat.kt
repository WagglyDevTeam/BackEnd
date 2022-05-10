package team.waggly.backend.model

import javax.persistence.*

@Entity
class HashTagForGroupChat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val Id: Long? = null,

    @Column
    @ManyToOne
    val groupChat: GroupChat,

    @Column
    @ManyToOne
    val hashTag: HashTag
)
