package team.waggly.backend.repository.querydsl

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.QueryResults
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.dto.post.PostDto
import team.waggly.backend.dto.post.QPostDto
import team.waggly.backend.model.QPost
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class QPostRepository {
    @PersistenceContext
    lateinit var em: EntityManager

    fun searchPostsByCollege(collegeType: CollegeType?, pageable: Pageable): PageImpl<PostDto> {
        val post = QPost.post

        val query = JPAQuery<Any>(em)
        val builder = BooleanBuilder()

        builder.and(post.activeStatus.eq(ActiveStatusType.ACTIVE))
        collegeType?.run { builder.and(post.college.eq(this)) }

        query.from(post)
                .where(builder)
                .orderBy(post.id.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())

        val results: QueryResults<PostDto> = query.select(
                QPostDto(
                        post.id,
                        post.title,
                        post.description,
                        post.createdAt,
                        Expressions.asNumber(0),
                        Expressions.asNumber(0),
                        Expressions.asNumber(0),
                        Expressions.asBoolean(false),
                        CaseBuilder()
                                .`when`(post.activeStatus.eq(ActiveStatusType.INACTIVE)).then(true)
                                .otherwise(false),
                        CaseBuilder()
                                .`when`(post.isAnonymous.ne(0)).then(true)
                                .otherwise(false)
                )
        ).fetchResults()

        return PageImpl(results.results, pageable, results.total)
    }
}