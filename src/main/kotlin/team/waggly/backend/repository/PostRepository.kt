package team.waggly.backend.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.Post

@Repository
interface PostRepository: JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE active_status='ACTIVE'")
    fun findAllActivePosts(): List<Post>

    @Query("SELECT p FROM Post p WHERE active_status='ACTIVE' and college LIKE CONCAT('%',:college,'%')")
    fun findActivePostsByCollegeByOrderByIdDesc(college: String): List<Post>

//    @Query("SELECT p FROM Post p WHERE college LIKE CONCAT('%',:college,'%')")
//    fun findAllByCollegeByOrderByIdDesc(college: String, pageable: Pageable): Page<Post>?
}