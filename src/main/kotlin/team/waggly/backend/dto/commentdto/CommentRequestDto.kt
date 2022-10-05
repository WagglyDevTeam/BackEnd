package team.waggly.backend.dto.commentdto

import com.fasterxml.jackson.annotation.JsonProperty
import team.waggly.backend.model.Comment
import team.waggly.backend.model.Post
import team.waggly.backend.model.User

data class CommentRequestDto(
    val commentDesc : String,
    @get:JsonProperty("isAnonymous")
    val isAnonymous : Boolean = false   //column 생성 ? 혹은 판단 후 user.nickname 을 익명으로? 혹은 이넘으로 처리?
){
    fun toEntity(post : Post, user: User) = Comment(
        post = post,
        user = user,
        description = commentDesc,
        isAnonymous = if (isAnonymous) 1 else 0
    )
}
