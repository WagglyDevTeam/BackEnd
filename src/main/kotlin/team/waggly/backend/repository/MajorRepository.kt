package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import team.waggly.backend.model.Major
import team.waggly.backend.model.Post
import team.waggly.backend.model.PostLike
import team.waggly.backend.model.University

@Repository
interface MajorRepository : JpaRepository<Major, Long> {
    fun findAllByUniversityOrderByMajorNameAsc(university: University): List<Major>?
}

