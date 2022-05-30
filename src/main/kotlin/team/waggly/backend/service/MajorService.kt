package team.waggly.backend.service

import org.springframework.stereotype.Service
import team.waggly.backend.dto.major.MajorResponseDto
import team.waggly.backend.dto.major.MajorSearchReqeustDto
import team.waggly.backend.repository.querydsl.QMajorRepository

@Service
class MajorService(val qMajorRepository: QMajorRepository){
    fun searchMajor(majorSearchReqeustDto: MajorSearchReqeustDto): MutableList<MajorResponseDto> {
        var majorResponseDtolist = mutableListOf<MajorResponseDto>()
        val majorSearchList = qMajorRepository.selectAllMajor(majorSearchReqeustDto.university,majorSearchReqeustDto.searchMajor)
        majorSearchList.map {
            majorResponseDtolist.add(MajorResponseDto.fromEntity(it))
        }
        return majorResponseDtolist
    }
}