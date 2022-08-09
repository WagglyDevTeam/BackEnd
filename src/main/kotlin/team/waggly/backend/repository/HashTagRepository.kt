package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.HashTag
import team.waggly.backend.model.Post

@Repository
interface HashTagRepository : JpaRepository<HashTag, Long> {
    fun findAllByPostAndDeletedAtNull(post: Post): List<HashTag>
}