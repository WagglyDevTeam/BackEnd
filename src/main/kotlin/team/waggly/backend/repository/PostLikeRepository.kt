package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.model.Post
import team.waggly.backend.model.PostLike

//interface PostLikeRepository : JpaRepository<PostLike, Long>, PostLikeRepositoryCustom {
interface PostLikeRepository : JpaRepository<PostLike, Long> {
    @Query("select a.id from post a join post_like b on a.id=b.post_id where college=:college " +
            "group by post_id order by COUNT(post_id) desc limit 1",
        nativeQuery = true)
    fun getMostLikedPostInCollege(college: String): Long?

    fun countByPostId(postId: Long): Int
    fun countByPostIdAndStatus(postId: Long, status: ActiveStatusType): Int

    fun existsByIdAndUserIdAndStatus(postId: Long, userId: Long, status: ActiveStatusType): Boolean

    fun findByPostAndUserId(post: Post, userId: Long): PostLike?

}

//
//interface PostLikeRepositoryCustom {
//
//    fun getBestPostLikePostId(): Long?
//}
//
//class PostLikeRepositoryImpl(
//    private val query: JPAQueryFactory
//): QuerydslRepositorySupport(PostLike::class.java), PostLikeRepositoryCustom {
//
//    override fun getBestPostLikePostId(): Long? {
//        val table = postLike
//        return query
//            .from(table)
//            .select(postLike.id)
//            .groupBy(postLike.post.count())
//            .orderBy(postLike.post.count().desc())
//            .fetchOne()
//    }
//}
