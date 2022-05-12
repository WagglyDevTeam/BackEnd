package team.waggly.backend.model

import javax.persistence.*

@Entity
class HashTagForGroupChat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val Id: Long? = null,

    @ManyToOne
    val groupChat: GroupChat,

    @ManyToOne
    val hashTag: HashTag
)
