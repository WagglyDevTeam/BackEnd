package team.waggly.backend.model

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
    val isAccepted: Boolean,

    @Column(columnDefinition = "VARCHAR(100)")
    val chatPurpose: String,

    @Column(columnDefinition = "VARCHAR(100)")
    val chatClass: String, // 선배 후배 동기 선택하는거임
)