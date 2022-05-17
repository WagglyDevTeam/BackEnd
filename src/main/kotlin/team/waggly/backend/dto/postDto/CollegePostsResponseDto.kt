package team.waggly.backend.dto.postDto

data class CollegePostsResponseDto(
    val bestPost: PostDetailsResponseDto,
    val posts: List<PostDetailsResponseDto>,
)