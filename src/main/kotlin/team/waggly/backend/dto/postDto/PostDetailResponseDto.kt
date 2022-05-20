package team.waggly.backend.dto.postDto

import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.model.Post
import java.time.LocalDateTime

data class PostDetailResponseDto(
    var postId: Long?,
    var postTitle: String,
    var postDesc: String,
    var postCreatedAt: LocalDateTime,
    var postImages: MutableList<String>,
    var postLikeCnt: Int?,
    var postCommentCnt: Int?,
    var isLikedByMe: Boolean?,
    var authorId: Long,
    //        var authorMajor: String,
    var authorNickname: String,
    var isBlind: Boolean,
    var isAnonymous: Boolean,
) {
    constructor(post: Post): this(
        post.id,
        post.title,
        post.description,
        post.createdAt,
        arrayListOf(),
        null,
        null,
        null,
        post.author.id!!,
        post.author.nickName,
        post.activeStatus == ActiveStatusType.INACTIVE,
        post.isAnonymous != 0,
    )
}