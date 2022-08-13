package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.PostLike

@Repository
//interface PostLikeRepository : JpaRepository<PostLike, Long>, PostLikeRepositoryCustom {
interface PostLikeRepository : JpaRepository<PostLike, Long> {
    @Query("SELECT post_id FROM post_like group by post_id order by COUNT(post_id) DESC LIMIT 1", nativeQuery = true)
    fun getMostLikedPostId(): Long?

    @Query("select a.id from post a join post_like b on a.id=b.post_id where college=:college " +
            "group by post_id order by COUNT(post_id) desc limit 1",
        nativeQuery = true)
    fun getMostLikedPostInCollege(college: CollegeType): Long?

    fun countByPostId(postId: Long): Int
    fun existsByUserId(userId: Long): Boolean
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