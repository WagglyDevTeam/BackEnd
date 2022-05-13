package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import team.waggly.backend.model.PostLike

@Repository
interface PostLikeRepository : JpaRepository<PostLike, Long> {
    @Query("SELECT post_id, COUNT(post_id) as cnt FROM Post_like group by post_id order by cnt DESC LIMIT 1", nativeQuery = true)
    fun findBestPostId(): Long
}