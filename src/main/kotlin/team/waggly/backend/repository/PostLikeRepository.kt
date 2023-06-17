package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.model.Post
import org.springframework.stereotype.Repository
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.PostLike

@Repository
interface PostLikeRepository : JpaRepository<PostLike, Long> {
    @Query("select a.id from post a left join post_like b on a.id=b.post_id where a.college = :college " +
            "group by post_id order by COUNT(post_id) desc limit :count",
        nativeQuery = true)
    fun getMostLikedPostInCollege(college: String, count: Int): List<Long>

    @Query("select a.id from post a left join post_like b on a.id=b.post_id where a.college = :college " +
            "group by b.post_id order by COUNT(b.post_id) desc limit :count",
            nativeQuery = true)
    fun getMostLikedPostsInCollege(college: String, count: Int): List<Long>

    fun countByPostId(postId: Long): Int
    fun countByPostIdAndStatus(postId: Long, status: ActiveStatusType): Int

    fun existsByPostIdAndUserIdAndStatus(postId: Long, userId: Long, status: ActiveStatusType): Boolean

    fun findByPostAndUserId(post: Post, userId: Long): PostLike?
}
