package team.waggly.backend.dto.user

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class CheckNicknameRequestDto(
    @field:NotBlank
    @field:Length(min = 2, max = 6, message = "2글자 이상 6글자 이하이여야 됩니다.")
    val nickname: String
)
