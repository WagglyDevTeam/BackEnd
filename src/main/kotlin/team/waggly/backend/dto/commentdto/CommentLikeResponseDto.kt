package team.waggly.backend.dto.commentdto

import com.fasterxml.jackson.annotation.JsonProperty

data class CommentLikeResponseDto(
        @get:JsonProperty("isLikedByMe")
        val isLikedByMe: Boolean,
        val commentLikeCnt: Int
)