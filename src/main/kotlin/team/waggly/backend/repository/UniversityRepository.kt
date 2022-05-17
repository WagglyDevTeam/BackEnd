package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.waggly.backend.model.University

interface UniversityRepository : JpaRepository<University, Long> {
    fun findByUniversityEmail(email: String): University?
}