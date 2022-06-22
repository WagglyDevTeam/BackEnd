package team.waggly.backend.dto.postDto

data class PostDetailResponseDto (
    val post: PostDetailDto,
    val comments: List<PostDetailCommentDto>
)