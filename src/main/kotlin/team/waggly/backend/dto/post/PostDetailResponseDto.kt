package team.waggly.backend.dto.post

data class PostDetailResponseDto (
    val post: PostDetailDto,
    val comments: List<PostDetailCommentDto>
)