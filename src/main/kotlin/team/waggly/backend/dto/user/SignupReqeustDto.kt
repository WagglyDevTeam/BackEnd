package team.waggly.backend.dto.user

import javax.validation.constraints.*

data class SignupReqeustDto(
    // 이메일 형식 빈칸금지 중복금지
    @field:Email(message = "이메일 형식이 아닙니다.")
    @field:NotBlank(message = "공백 불가 입니다.")
    val email: String,
    // 비밀번호 형식 맞는지 체크
    @field:NotBlank(message = "공백 불가 입니다.")
    @field:Pattern(regexp = "((?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,16})",message = "비밀번호 형식이 맞지 않습니다.")
    val password: String,
    // 닉네임 형식 맞는지 체크 중복 금지
    @field:NotBlank(message = "공백 불가 입니다.")
    @field:Pattern(regexp = "^[A-Za-z0-9가-힣]{2,6}$",message = "닉네임 형식이 맞지않습니다.")
    val nickname: String,
    // 빈칸금지
    @field:NotBlank(message = "공백 불가 입니다.")
    val university: String,
    // 빈칸 금지 100글자 이상 금지
    @field:NotNull(message = "공백 불가 입니다.")
    val classNumber: Int,
    // 빈칸 금지
    @field:NotNull(message = "공백 불가 입니다.")
    val major: Long,
    // 빈칸금지
    @field:NotBlank(message = "공백 불가 입니다.")
    val gender: String
)
