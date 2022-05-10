package team.waggly.backend.model

import team.waggly.backend.commomenum.ChatPurposeType
import javax.persistence.*

@Entity
class RequestForChat (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    @ManyToOne
    val receiver: User,

    @Column
    @ManyToOne
    val sender: User,

    @Column(columnDefinition = "VARCHAR(100)")
    val requestMsg: String,

    @Column
    val isAccepted: Boolean = false,

    @Column(columnDefinition = "VARCHAR(100)")
    val chatPurpose: ChatPurposeType,

    @Column(columnDefinition = "VARCHAR(100)")
    val chatClass: chatClassType, // 선배 후배 동기 선택하는거임
) {
    enum class chatClassType {
        SENIOR,
        MATE,
        JUNIOR
    }
}