package team.waggly.backend.dto.postDto

import team.waggly.backend.commomenum.CollegeType

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
            val postTitle: String,
    )
}