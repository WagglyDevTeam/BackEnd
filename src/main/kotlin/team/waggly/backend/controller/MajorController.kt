package team.waggly.backend.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.major.MajorResponseDto
import team.waggly.backend.dto.major.MajorSearchReqeustDto
import team.waggly.backend.service.MajorService

@RestController
class MajorController(val majorService: MajorService){

    @PostMapping("/major")
    fun searchMajor(
        @RequestBody majorSearchReqeustDto: MajorSearchReqeustDto,
    ): ResponseDto<MutableList<MajorResponseDto>> {
        return ResponseDto(majorService.searchMajor(majorSearchReqeustDto))
    }
}