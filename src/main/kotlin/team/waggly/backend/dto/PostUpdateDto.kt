package team.waggly.backend.dto

import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.Post
import java.time.LocalDateTime

class PostUpdateDto(
        private val title: String?,
        private val description: String?,
        private val college: CollegeType?,
) {
    fun updateEntity(post: Post) {
            post.title = title ?: post.title
            post.description = description ?: post.description
            post.college = college ?: post.college
            post.modifiedAt = LocalDateTime.now()
    }
}