package team.waggly.backend.dto.post

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.domain.Pageable

class SearchPostRequest(
        val keyword: String,

        @JsonIgnore
        var pageable: Pageable? = null,
)