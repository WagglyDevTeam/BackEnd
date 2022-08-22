package team.waggly.backend.dto.home

import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.dto.postDto.PostSummaryResponseDto

data class HomeResponseDto(
    val bestPostWithCollegeType : Pair<CollegeType,PostSummaryResponseDto>,
    val randomBestPostSummary: PostSummaryResponseDto
)