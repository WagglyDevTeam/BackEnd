package team.waggly.backend.dto.post

import com.fasterxml.jackson.annotation.JsonProperty

class PostLikeResponseDto(
        @get:JsonProperty("isLikedByMe")
        val isLikedByMe: Boolean,
        val postLikeCnt: Int,
)