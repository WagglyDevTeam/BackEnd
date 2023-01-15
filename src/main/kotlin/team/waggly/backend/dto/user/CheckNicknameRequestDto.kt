package team.waggly.backend.dto.user

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class CheckNicknameRequestDto(
    @field:NotBlank(message = "닉네임 입력을 해주세요.")
    @field:Pattern(regexp = "^[A-Za-z0-9가-힣]{2,6}$",message = "닉네임 형식이 맞지않습니다.")
    val nickname: String
)
