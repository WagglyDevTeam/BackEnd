package team.waggly.backend.dto.chat

import team.waggly.backend.model.User

class GetRequestForChatRecommendResponse(
        val userId: Long,
        val nickName: String,
        val major: String,
        val classNumber: Int,
        val introduction: String?,
) {
    constructor(user: User) : this(
            user.id!!,
            user.nickName,
            user.major.majorName,
            user.classNum,
            user.introduction
    )
}