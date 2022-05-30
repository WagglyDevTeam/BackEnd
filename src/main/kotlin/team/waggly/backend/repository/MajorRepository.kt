package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import team.waggly.backend.model.Major

@Repository
interface MajorRepository : JpaRepository<Major, Long> {
}

