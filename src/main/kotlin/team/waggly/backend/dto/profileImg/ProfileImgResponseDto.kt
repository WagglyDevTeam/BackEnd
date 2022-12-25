package team.waggly.backend.dto.profileImg

import team.waggly.backend.commomenum.ActiveStatusType

class ProfileImgResponseDto (
    val id: Long?,
    val url: String,
    val color: String,
    val colorNumber: Int,
    val activeStatus: ActiveStatusType,
)