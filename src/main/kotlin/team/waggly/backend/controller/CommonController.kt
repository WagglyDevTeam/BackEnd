package team.waggly.backend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.profileImg.ProfileImgResponseDto
import team.waggly.backend.service.CommonService

@RestController
class CommonController (private val commonService: CommonService) {
    @GetMapping("/_c/profile/defaultImg")
    fun getHome(): ResponseDto<Any> {
        return ResponseDto(
            datas = commonService.getProfileImgs()
        )
    }
}