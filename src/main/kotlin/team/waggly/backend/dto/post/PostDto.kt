package team.waggly.backend.dto.post

import com.querydsl.core.annotations.QueryProjection
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.model.Post
import java.time.LocalDateTime

// 수정
data class PostDto @QueryProjection constructor(
    val postId: Long?,
    val postTitle: String,
    val postDesc: String,
    val postCreatedAt: LocalDateTime,
//        var authorMajor: String,
    var postImageCnt: Int?,
    var postLikeCnt: Int?,
    var postCommentCnt: Int?,
    var isLikedByMe: Boolean?,
    val isBlind: Boolean,
    val isAnonymous: Boolean,
) {
    constructor(post: Post): this(
        post.id,
        post.title,
        post.description,
        post.createdAt,
            null,
        null,
        null,
        null,
        post.activeStatus == ActiveStatusType.INACTIVE,
        post.isAnonymous != 0,
    )
}