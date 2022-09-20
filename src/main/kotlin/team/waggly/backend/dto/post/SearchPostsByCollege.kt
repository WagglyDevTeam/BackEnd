package team.waggly.backend.dto.post

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.domain.Pageable
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.User

class SearchPostsByCollege(
        val college: CollegeType,

        @JsonIgnore
        var pageable: Pageable? = null,
        @JsonIgnore
        var user: User? = null
)