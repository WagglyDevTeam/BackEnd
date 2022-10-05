package team.waggly.backend.dto.post

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.multipart.MultipartFile
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.Post
import team.waggly.backend.model.User
import javax.validation.constraints.NotBlank

data class CreatePostRequestDto (
    @field:NotBlank
    val title: String?,

    @field:NotBlank
    val description: String?,

    val college: CollegeType,

    val file: List<MultipartFile>?,

    @get:JsonProperty("isAnonymous")
    val isAnonymous: Boolean = false,
) {
    fun toEntity(user: User): Post = Post(
        title = title ?: "",
        description = description ?: "",
        college = college,
        author = user,
        isAnonymous = if (isAnonymous) 1 else 0
    )
}