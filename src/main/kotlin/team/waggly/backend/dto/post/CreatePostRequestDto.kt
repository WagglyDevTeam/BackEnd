package team.waggly.backend.dto.post

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.multipart.MultipartFile
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.Post
import team.waggly.backend.model.User
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class CreatePostRequestDto (
    @field:NotBlank
    @Size(min=5, max=20, message="제목은 5글자 이상 20글자 이하로 작성해야 됩니다.")
    val title: String?,

    @field:NotBlank
    @Size(min=5, max=200, message="본문은 5글자 이상 200글자 이하로 작성해야 됩니다.")
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