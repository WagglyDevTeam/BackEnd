package team.waggly.backend.dto.myPageDto

import team.waggly.backend.model.Comment
import java.time.LocalDateTime

class MyCommentsDetailDto (
    val commentId: Long?,
    val commentCreatedAt: LocalDateTime,
    val commentDesc: String,
    val postId: Long,
    val postTitle: String,
    val boardType: String,
) {
    constructor(comment: Comment): this(
        comment.id,
        comment.createdAt,
        comment.description,
        comment.post.id!!,
        comment.post.title,
        comment.post.college.desc,
    )
}