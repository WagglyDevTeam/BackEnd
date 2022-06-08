package team.waggly.backend.dto.commentdto

import team.waggly.backend.model.Comment
import team.waggly.backend.model.User

data class ReplyRequestDto(
    val replyDesc : String,
    val isAnonymous : Boolean = false
){
    fun toEntity(comment: Comment, user: User) = Comment(
        post = comment.post,
        user = user,
        parentComment = comment,
        description = replyDesc,
        isAnonymous = if (isAnonymous) 1 else 0
    )
}

