package team.waggly.backend.dto.email

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

class EmailRequestDto (
    @field:Email(message = "이메일 형식이 아닙니다.")
    @field:NotBlank(message = "공백 불가 입니다.")
    val email: String
    )