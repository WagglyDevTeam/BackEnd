package team.waggly.backend.dto.post

data class SearchPostsByCollegeResponseDto(
        val bestPost: PostDto?,
        val posts: List<PostDto>,
)
