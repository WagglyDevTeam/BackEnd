package team.waggly.backend.dto.post

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.User

class SearchPostsByCollege(
        val college: CollegeType? = null,
        @PageableDefault(size = 10, page = 0)
        val pageable: Pageable,

        @JsonIgnore
        var user: User? = null
)