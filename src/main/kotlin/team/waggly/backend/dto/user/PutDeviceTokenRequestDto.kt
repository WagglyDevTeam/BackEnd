package team.waggly.backend.dto.user

import javax.validation.constraints.NotBlank

data class PutDeviceTokenRequestDto (
    @field:NotBlank(message = "Device Token을 입력해주세요.")
    val deviceToken: String
)