package team.waggly.backend.service

import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.dto.major.MajorResponseDto
import team.waggly.backend.dto.major.MajorSearchReqeustDto
import team.waggly.backend.model.Major
import team.waggly.backend.repository.MajorRepository
import team.waggly.backend.repository.UniversityRepository
import team.waggly.backend.repository.querydsl.QMajorRepository

@Service
class MajorService(val qMajorRepository: QMajorRepository, val majorRepository: MajorRepository, val universityRepository: UniversityRepository){
    fun searchMajor(majorSearchReqeustDto: MajorSearchReqeustDto): MutableList<MajorResponseDto> {
        var majorResponseDtolist = mutableListOf<MajorResponseDto>()
        val majorSearchList = qMajorRepository.selectAllMajor(majorSearchReqeustDto.universityId,majorSearchReqeustDto.searchMajor)
        majorSearchList.map {
            majorResponseDtolist.add(MajorResponseDto.fromEntity(it))
        }
        return majorResponseDtolist
    }

    fun getMajorListByUniversityName(universityName: String): MutableList<MajorResponseDto> {
        var majorResponseDtolist = mutableListOf<MajorResponseDto>()
        val university = universityRepository.findByUniversityName(universityName)
        if (university != null) {
            val majorList = majorRepository.findAllByUniversityAndMajorStatusOrderByMajorNameAsc(university, ActiveStatusType.ACTIVE)
            majorList?.map {
                majorResponseDtolist.add(MajorResponseDto.fromEntity(it))
            }
            return majorResponseDtolist
        }
        return majorResponseDtolist
    }
}