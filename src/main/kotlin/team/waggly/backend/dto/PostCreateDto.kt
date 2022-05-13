package team.waggly.backend.dto

import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.Post
import team.waggly.backend.model.User

data class PostCreateDto (
        private val title: String,
        private val description: String,
        private val college: CollegeType,
        private val classNum: Int,
) {
    fun toEntity(author: User): Post = Post(
            title = title,
            description = description,
            college = college,
            author = author,
    )
}