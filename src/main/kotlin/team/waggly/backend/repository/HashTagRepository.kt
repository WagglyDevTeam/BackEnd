package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.model.HashTag

@Repository
interface HashTagRepository : JpaRepository<HashTag, Long> {
}