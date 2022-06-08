package team.waggly.backend.dto.postDto

data class PostLikeResponseDto (
    val isLikedByMe: Boolean,
    val postLikeCnt: Int,
)