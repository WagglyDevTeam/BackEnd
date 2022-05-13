package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository

interface CommentLikeRepository : JpaRepository<CommentLikeRepository , Long> {
}