package team.waggly.backend.dto.post

import org.springframework.web.multipart.MultipartFile
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.Post
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class UpdatePostRequestDto(
        @field:NotBlank
        @Size(min=5, max=20, message="제목은 5글자 이상 20글자 이하로 작성해야 됩니다.")
        val title: String?,

        @field:NotBlank
        @Size(min=5, max=200, message="본문은 5글자 이상 200글자 이하로 작성해야 됩니다.")
        val description: String?,

        @field:NotNull(message = "학부를 선택해주세요.")
        val college: CollegeType?,
        val file: List<MultipartFile>?,
        val deleteTargetUrl: List<String>?,
) {
    fun updateEntity(post: Post) {
        post.title = title ?: post.title
        post.description = description ?: post.description
        post.college = college ?: post.college
        post.modifiedAt = LocalDateTime.now()
    }
}
