package team.waggly.backend.dto.postDto

class PostDetailResponseDto (
    val post: PostDetailDto,
    val comments: List<PostDetailCommentDto>
)