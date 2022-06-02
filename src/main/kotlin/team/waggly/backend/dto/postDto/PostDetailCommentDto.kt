package team.waggly.backend.dto.postDto

import java.time.LocalDateTime

class PostDetailCommentDto (
    val commentId: Long,
    val commentCreatedAt: LocalDateTime,
    val commentLikeCnt: Int,
    val commentDesc: String,
    val isLikedByMe: Boolean,
    val authorId: Long,
//    val authorMajor: String,
    val authorNickname: String,
    val authorProfileImg: String,
    val isBlind: Boolean,
    val replies: List<PostDetailReplyDto>
)