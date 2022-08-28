package team.waggly.backend.dto.home

import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.dto.post.PostDto

data class HomeResponseDto(
    val bestPostWithCollegeType : Pair<CollegeType,PostDto>,
    val randomBestPostSummary: PostDto
)