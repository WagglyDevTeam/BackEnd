package team.waggly.backend.dto.myPageDto

import team.waggly.backend.model.Post
import java.time.LocalDateTime

data class MyPostsResponseDto (
    val postId: Long?,
    val postTitle: String,
    val postDesc: String,
    val postCreatedAt: LocalDateTime,
    //        var authorMajor: String,
    var postImageCnt: Int?,
    var postLikeCnt: Int?,
    var postCommentCnt: Int?,
    var isLikedByMe: Boolean?,
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
        post.isAnonymous != 0,
    )
}