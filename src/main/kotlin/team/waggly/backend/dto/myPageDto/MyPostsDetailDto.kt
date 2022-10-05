package team.waggly.backend.dto.myPageDto

import com.fasterxml.jackson.annotation.JsonProperty
import team.waggly.backend.model.Post
import java.time.LocalDateTime

data class MyPostsDetailDto (
    val postId: Long?,
    val postTitle: String,
    val postDesc: String,
    val postCreatedAt: LocalDateTime,
    var authorMajor: String?,
    var postImageCnt: Int?,
    var postLikeCnt: Int?,
    var postCommentCnt: Int?,
    @get:JsonProperty("isLikedByMe")
    var isLikedByMe: Boolean?,
    @get:JsonProperty("isAnonymous")
    val isAnonymous: Boolean,
) {
    constructor(post: Post): this(
        post.id,
        post.title,
        post.description,
        post.createdAt,
        post.author.major.majorName,
        null,
        null,
        null,
        null,
        post.isAnonymous != 0,
    )
}
