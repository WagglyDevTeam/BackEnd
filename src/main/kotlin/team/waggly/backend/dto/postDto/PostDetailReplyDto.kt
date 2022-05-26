package team.waggly.backend.dto.postDto

import java.time.LocalDateTime

class PostDetailReplyDto (
    val replyId: Long,
    val replyCreatedAt: LocalDateTime,
    val replyLikeCnt: Int,
    val replyDesc: String,
    val isLikedByMe: Boolean,
    val authorId: Long,
//    val authorMajor: String,
    val authorNickname: String,
    val authorProfileImg: String,
    val isBlind: Boolean,
)