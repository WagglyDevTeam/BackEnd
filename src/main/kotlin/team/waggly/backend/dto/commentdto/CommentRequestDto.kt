package team.waggly.backend.dto.commentdto

data class CommentRequestDto(
    val commentDesc : String,
    val isAnonymous : Boolean   //column 생성 ? 혹은 판단 후 user.nickname 을 익명으로? 혹은 이넘으로 처리?
)