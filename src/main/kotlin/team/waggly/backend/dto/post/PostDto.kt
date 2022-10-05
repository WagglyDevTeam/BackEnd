package team.waggly.backend.dto.post

import com.fasterxml.jackson.annotation.JsonProperty
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
        var authorMajor: String,
        var postImageCnt: Int?,
        var postLikeCnt: Int?,
        var postCommentCnt: Int?,
        @get:JsonProperty("isLikedByMe")
        var isLikedByMe: Boolean?,
        @get:JsonProperty("isBlind")
        val isBlind: Boolean,
        @get:JsonProperty("isAnonymous")
        val isAnonymous: Boolean,
) {
    constructor(post: Post) : this(
            post.id,
            post.title,
            post.description,
            post.createdAt,
            post.author.major.majorName,
            null,
            null,
            null,
            null,
            post.activeStatus == ActiveStatusType.INACTIVE,
            post.isAnonymous != 0,
    )
}