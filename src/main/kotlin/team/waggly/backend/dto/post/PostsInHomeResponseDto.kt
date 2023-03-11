package team.waggly.backend.dto.post

import team.waggly.backend.commomenum.CollegeType
import java.time.LocalDateTime

class PostsInHomeResponseDto(
        val userCollegePosts: CollegePosts,
        val otherCollegePosts: List<CollegePosts>
) {
    class CollegePosts(
            val collegeType: CollegeType,
            val collegeTypeName: String,
            val posts: List<PostHomeDto>
    )

    class PostHomeDto(
        val postId: Long,
        val majorName: String,
        val postTitle: String,
        val postCreatedAt: LocalDateTime,
    )
}