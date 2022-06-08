package team.waggly.backend.dto.postDto

import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.model.Comment
import team.waggly.backend.model.Post
import java.time.LocalDateTime

data class PostDetailCommentDto (
    val commentId: Long?,
    val commentCreatedAt: LocalDateTime,
    val commentLikeCnt: Int?,
    val commentDesc: String,
    val isLikedByMe: Boolean?,
    val authorId: Long?,
    val authorMajor: String,
    val authorNickname: String,
    val authorProfileImg: String,
    val isBlind: Boolean,
    val replies: List<PostDetailReplyDto>?
) {
    constructor(comment: Comment): this(
        comment.id,
        comment.createdAt,
        null,
        comment.description,
        null,
        comment.user.id,
        comment.user.major,
        comment.user.nickName,
        comment.user.profileImgUrl,
        comment.activeStatus == ActiveStatusType.INACTIVE,
        null,
    )
}