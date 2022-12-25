package team.waggly.backend.model

import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class ProfileImg (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(columnDefinition = "TIMESTAMP")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(columnDefinition = "TIMESTAMP")
    var modifiedAt: LocalDateTime = LocalDateTime.now(),

    @Column(columnDefinition = "VARCHAR(255)")
    val url: String,

    @Column(columnDefinition = "VARCHAR(255)")
    val path: String,

    @Column(columnDefinition = "VARCHAR(45)")
    val color: String,

    @Column(columnDefinition = "INT(4)")
    val colorNumber: Int,

    @Column(columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    var activeStatus: ActiveStatusType
    )