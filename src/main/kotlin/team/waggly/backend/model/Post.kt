package team.waggly.backend.model

import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.repository.PostLikeRepository
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
    @Enumerated(EnumType.STRING)
    var college: CollegeType, //단과대 enum

    @Column(columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    var activeStatus: ActiveStatusType = ActiveStatusType.ACTIVE, // enum

    @ManyToOne
    val author: User,

    @Column(columnDefinition = "TINYINT(1)")
    var isAnonymous: Int = 0,

    @Column
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column
    var modifiedAt: LocalDateTime = LocalDateTime.now(),

    @Column
    var deletedAt: LocalDateTime? = null,
) {
    companion object Additional {
        private lateinit var postLikeRepository: PostLikeRepository
        fun getLikeCnt(postId: Long, postLikeRepository: PostLikeRepository): Int {
            return postLikeRepository.countByPostId(postId)
        }
    }
}