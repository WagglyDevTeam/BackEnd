package team.waggly.backend.dto.commentdto

data class CommentRequestDto(
    val commentDesc : String,
    val isAnonymous : Boolean
)