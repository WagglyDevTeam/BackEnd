package team.waggly.backend.model

import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.dto.CreatePostDto
import java.time.LocalDateTime
import javax.persistence.*


@Entity
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(columnDefinition = "VARCHAR(255)")
    var title: String,

    @Column(columnDefinition = "VARCHAR(255)")
    var description: String,

    @Column(columnDefinition = "VARCHAR(30)")
    val college: CollegeType, //단과대 enum

    @Column(columnDefinition = "VARCHAR(10)")
    var activeStatus: ActiveStatusType = ActiveStatusType.ACTIVE, // enum

    @Column
    @ManyToOne
    val author: User,

    @Column
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column
    var modifiedAt: LocalDateTime = LocalDateTime.now()
){
}