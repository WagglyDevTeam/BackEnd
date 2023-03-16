package team.waggly.backend.dto.user

import team.waggly.backend.model.User

class UserLoginResponseDto(
        val userId: Long,
        val nickName: String,
        val university: String,
        val classNumber: Int,
        val college: String,
        val major: String,
        val gender: User.GenderType,
        val profileImg: String,
        val introduction: String?,
) {
    constructor(user: User) : this(
            userId = user.id!!,
            nickName = user.nickName,
            university = user.major.university.universityName,
            classNumber = user.classNum,
            college = user.major.college.toString(),
            major = user.major.majorName,
            gender = user.gender,
            profileImg = user.profileImgUrl,
            introduction = user.introduction
    )
}