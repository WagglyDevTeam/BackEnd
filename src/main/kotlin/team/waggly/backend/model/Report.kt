package team.waggly.backend.model

import javax.persistence.*

@Entity
class Report (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(columnDefinition = "VARCHAR(30)")
    val reportType: String,

    @Column(columnDefinition = "INTEGER(10)")
    val reportUserId: Long, // 신고한사람

    @Column(columnDefinition = "INTEGER(10)")
    val reportedUserId: Long, // 신고당한사람

    @Column(columnDefinition = "VARCHAR(100)")
    val reason: String
)