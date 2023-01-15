package team.waggly.backend.dto.user
import javax.validation.constraints.NotBlank

data class GetDeviceTokenRequestDto (
    @field:NotBlank(message = "유저 ID를 입력해주세요.")
    val userId: Long
)
