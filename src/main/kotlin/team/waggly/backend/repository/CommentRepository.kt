package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.model.Comment
import team.waggly.backend.model.User

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    fun countByPostId(postId: Long): Int
    fun findByUserAndActiveStatusOrderByCreatedAtDesc(user: User, activeStatus: ActiveStatusType): List<Comment>
}