package team.waggly.backend.dto.commentdto

data class ReplyRequestDto(
    val replyDesc : String,
    val isAnonymous : Boolean = false
)
