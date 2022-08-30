package team.waggly.backend.controller

import org.springframework.web.bind.annotation.*
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.major.MajorResponseDto
import team.waggly.backend.dto.major.MajorSearchReqeustDto
import team.waggly.backend.dto.post.SearchPostsByCollege
import team.waggly.backend.service.MajorService

@RestController
class MajorController(val majorService: MajorService){

    @PostMapping("/major")
    fun searchMajor(
        @RequestBody majorSearchReqeustDto: MajorSearchReqeustDto,
    ): ResponseDto<MutableList<MajorResponseDto>> {
        return ResponseDto(majorService.searchMajor(majorSearchReqeustDto))
    }

    @GetMapping("/major")
    fun getMajorListByUniversityName(
        @RequestParam university: String,
    ): ResponseDto<MutableList<MajorResponseDto>> {
        return ResponseDto(majorService.getMajorListByUniversityName(university))
    }
}