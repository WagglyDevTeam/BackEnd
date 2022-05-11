package team.waggly.backend.dto

import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.Post
import team.waggly.backend.model.User

data class CreatePostDto (
        val title: String,
        val description: String,
        val college: CollegeType,
        val activeStatus: ActiveStatusType,
        val author: User,
) {
    fun toEntity(): Post = Post(
            title = title,
            description = description,
            college = college,
            activeStatus = activeStatus,
            author = author,
    )
}