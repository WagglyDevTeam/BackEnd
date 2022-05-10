package team.waggly.backend.model

import team.waggly.backend.commomenum.CollegeType
import javax.persistence.*

@Entity
class Major (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    @ManyToOne
    val university: University,

    @Column(columnDefinition = "VARCHAR(30)")
    val majorName: String,

    @Column(columnDefinition = "VARCHAR(30)")
    val majorCode: String,

    @Column(columnDefinition = "VARCHAR(30)")
    val collegeEnum: CollegeType, // 이넘
)