package team.waggly.backend.repository.querydsl

import com.querydsl.jpa.JPAExpressions.select
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import team.waggly.backend.model.Major
import team.waggly.backend.model.QMajor.major
import team.waggly.backend.model.QUniversity.university

@Repository
class QMajorRepository (
    val jpaQueryFactory: JPAQueryFactory
        ){
     fun selectAllMajor(universityName: String, majorName: String): List<Major>{
          return jpaQueryFactory.selectFrom(major)
              .join(major.university, university)
              .where(university.universityName.eq(universityName).and(major.majorName.contains(majorName)))
             .fetch()
    }
}