package team.waggly.backend.dto.post

data class SearchPostsByCollegeResponseDto(
        val bestPost: List<PostDto>,
        val posts: List<PostDto>,
)
