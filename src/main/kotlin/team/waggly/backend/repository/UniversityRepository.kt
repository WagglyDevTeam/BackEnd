package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
<<<<<<< HEAD
import org.springframework.stereotype.Repository
import team.waggly.backend.model.University

@Repository
interface UniversityRepository : JpaRepository<University, Long> {
=======
import team.waggly.backend.model.University

interface UniversityRepository : JpaRepository<University, Long> {
    fun findByUniversityEmail(email: String): University?
>>>>>>> 17a95112e49562fb52eeb4f8841ed553a3880a4b
}