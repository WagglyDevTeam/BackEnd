package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.model.University

@Repository
interface UniversityRepository : JpaRepository<University, Long> {
    fun findByUniversityEmail(email: String): University?
    fun findByUniversityName(universityName: String): University?
}