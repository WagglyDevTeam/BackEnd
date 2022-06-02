package team.waggly.backend.dto.major

import team.waggly.backend.model.Major

data class MajorResponseDto(
    val majorId: Long,
    val universityId: Long,
    val majorName: String
){
    companion object{
        fun fromEntity(major: Major): MajorResponseDto{
            return major.run {
                MajorResponseDto(majorId = id!!, universityId = university.Id!!, majorName = majorName)
            }
        }
    }
}
