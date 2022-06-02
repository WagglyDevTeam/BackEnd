package team.waggly.backend.dto.postDto

data class CollegePostsResponseDto(
    val bestPost: PostSummaryResponseDto?,
    val posts: List<PostSummaryResponseDto>?,
)