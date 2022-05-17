package team.waggly.backend.dto.postDto

import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.Post
import java.time.LocalDateTime

data class UpdatePostRequestDto(
    val title: String?,
    val description: String?,
    val college: CollegeType?,
) {
    fun updateEntity(post: Post) {
        post.title = title ?: post.title
        post.description = description ?: post.description
        post.college = college ?: post.college
        post.modifiedAt = LocalDateTime.now()
    }
}
