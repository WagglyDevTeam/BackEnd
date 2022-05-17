package team.waggly.backend.dto.commentdto

data class CommentLikeResponseDto(
    val isLikedByMe : Boolean,
    val commentLikeCnt : Int
)