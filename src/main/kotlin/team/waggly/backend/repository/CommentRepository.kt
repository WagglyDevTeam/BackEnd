package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.model.Comment

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    fun countByPostId(postId: Long): Int
}