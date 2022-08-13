package team.waggly.backend.dto.post

import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.model.Comment
import java.time.LocalDateTime

data class PostDetailReplyDto (
    val replyId: Long,
    val replyCreatedAt: LocalDateTime,
    var replyLikeCnt: Int?,
    val replyDesc: String,
    var isLikedByMe: Boolean?,
    val authorId: Long,
    val authorMajor: String,
    val authorNickname: String,
    val authorProfileImg: String,
    val isBlind: Boolean,
) {
    constructor(comment: Comment): this(
        comment.id!!,
        comment.createdAt,
        null,
        comment.description,
        null,
        comment.user.id!!,
        comment.user.major.majorName,
        comment.user.nickName,
        comment.user.profileImgUrl,
        comment.activeStatus == ActiveStatusType.INACTIVE,
    )
}