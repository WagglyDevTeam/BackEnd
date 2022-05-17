package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.model.CommentLike

@Repository
interface CommentLikeRepository : JpaRepository<CommentLike, Long> {
    fun existsByUserIdAndCommentId(userId: Long, commentId: Long): Boolean
    fun findByUserIdAndCommentIdOrNull(userId: Long, commentId: Long) : CommentLike//유일
    fun countByCommentId(commentId: Long): Int
}