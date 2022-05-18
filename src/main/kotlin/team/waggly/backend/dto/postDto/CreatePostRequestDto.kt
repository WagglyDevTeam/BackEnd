package team.waggly.backend.dto.postDto

import org.springframework.web.multipart.MultipartFile
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.Post
import team.waggly.backend.model.User
import javax.validation.constraints.NotBlank

data class CreatePostRequestDto (
    @field:NotBlank(message = "제목을 입력해주세요.")
    val title: String,

    @field:NotBlank(message = "내용을 입력해주세요.")
    val description: String,

    val college: CollegeType,

    val file: List<MultipartFile>?,

    val isAnonymous: Boolean = false,
) {
    fun toEntity(user: User): Post = Post(
        title = title,
        description = description,
        college = college,
        author = user,
        isAnonymous = if (isAnonymous) 1 else 0
    )
}