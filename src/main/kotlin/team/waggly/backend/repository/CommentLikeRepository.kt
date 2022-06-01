package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.model.CommentLike

@Repository
interface CommentLikeRepository : JpaRepository<CommentLike, Long> {
    fun findByUserIdAndCommentId(userId: Long, commentId: Long) : CommentLike?
    fun countByCommentIdAndActiveStatus(commentId: Long, activeStatusType: ActiveStatusType): Int
}