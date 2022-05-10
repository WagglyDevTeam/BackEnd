package team.waggly.backend.model

import javax.persistence.*

@Entity
class Alarm (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(columnDefinition = "VARCHAR(10)")
    val alarmType: AlarmType,

    @Column(columnDefinition = "INTEGER(10)", nullable = true)
    val targetId: Long?, // 타입에 따라 전환해 줄 컨텐츠

    @Column(columnDefinition = "INTEGER(10)")
    val receiverId: Long //userId
) {
    enum class AlarmType {
        POST,
        COMMENT,
        CHATREQUEST,
        BAN
    }
}