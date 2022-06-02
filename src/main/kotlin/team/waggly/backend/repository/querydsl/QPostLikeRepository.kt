package team.waggly.backend.repository.querydsl

import com.querydsl.core.types.ExpressionUtils.count
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.Wildcard.count
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.QPost.post
import team.waggly.backend.model.QPostLike.postLike

@Repository
class QPostLikeRepository (
    val jpaQueryFactory: JPAQueryFactory
        ) {
    fun getMostLikedPostInCollege(college: CollegeType): Long? {
        return jpaQueryFactory.selectFrom(post)
            .select(post.id)
            .join(postLike)
            .on(postLike.post.eq(post))
            .where(post.college.eq(college))
            .groupBy(post.id)
            .orderBy(postLike.post.count().desc())
            .limit(1)
            .fetchFirst()
    }
}