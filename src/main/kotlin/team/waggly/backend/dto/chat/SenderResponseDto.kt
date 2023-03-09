package team.waggly.backend.dto.chat

import team.waggly.backend.model.User

class SenderResponseDto (
        val userId: Long,
        val nickName: String,
        val major: String,
        val profileImg: String,
){
    constructor(user : User) : this(
            userId = user.id!!,
            nickName = user.nickName,
            major = user.major.majorName,
            profileImg = user.profileImgUrl,
    )
}