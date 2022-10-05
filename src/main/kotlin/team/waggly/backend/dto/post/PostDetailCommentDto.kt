package team.waggly.backend.dto.post

import com.fasterxml.jackson.annotation.JsonProperty
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.model.Comment
import java.time.LocalDateTime

data class PostDetailCommentDto (
    val commentId: Long?,
    val commentCreatedAt: LocalDateTime,
    var commentLikeCnt: Int?,
    val commentDesc: String,
    @get:JsonProperty("isLikedByMe")
    var isLikedByMe: Boolean?,
    val authorId: Long?,
    val authorMajor: String?,
    val authorNickname: String,
    val authorProfileImg: String,
    @get:JsonProperty("isBlind")
    val isBlind: Boolean,
    @get:JsonProperty("isAnonymous")
    val isAnonymous: Int,
    var replies: List<PostDetailReplyDto>
) {
    constructor(comment: Comment): this(
        comment.id,
        comment.createdAt,
        null,
        comment.description,
        null,
        comment.user.id,
        comment.user.major.majorName,
        comment.user.nickName,
        comment.user.profileImgUrl,
        comment.activeStatus == ActiveStatusType.INACTIVE,
            comment.isAnonymous,
        arrayListOf(),
    )
}