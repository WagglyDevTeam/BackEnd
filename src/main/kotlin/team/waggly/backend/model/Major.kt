package team.waggly.backend.model

import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import javax.persistence.*

@Entity
class Major (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    val university: University,

    @Column(columnDefinition = "VARCHAR(255)")
    val majorName: String,

    @Column(columnDefinition = "VARCHAR(30)")
    @Enumerated(EnumType.STRING)
    val collegeEnum: CollegeType, // 이넘

    @Column
    @Enumerated(EnumType.STRING)
    val majorStatus: ActiveStatusType = ActiveStatusType.ACTIVE
)