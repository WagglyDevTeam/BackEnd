package team.waggly.backend.dto.commentdto

import com.fasterxml.jackson.annotation.JsonProperty
import team.waggly.backend.model.Comment
import team.waggly.backend.model.User

data class ReplyRequestDto(
    val replyDesc : String,
    @get:JsonProperty("isAnonymous")
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

