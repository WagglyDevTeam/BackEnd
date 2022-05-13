package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.Post

@Repository
interface PostRepository: JpaRepository<Post, Long> {
    fun findAllByCollege(college: CollegeType): Array<Post>
}