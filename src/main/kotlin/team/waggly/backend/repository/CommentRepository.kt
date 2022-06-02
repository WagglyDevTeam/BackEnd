package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.model.Comment
import team.waggly.backend.model.User

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    // MyPageController.getAllMyPosts - 내가 쓴 게시글 전체 리스트
    fun findByUserAndActiveStatusOrderByCreatedAtDesc(user: User, activeStatus: ActiveStatusType): List<Comment>

    // 게시글의 댓글 수
    fun countByPostId(postId: Long): Int
}