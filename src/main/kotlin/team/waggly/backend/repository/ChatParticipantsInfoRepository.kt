package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.model.ChatParticipantsInfo
import team.waggly.backend.model.User

@Repository
interface ChatParticipantsInfoRepository : JpaRepository<ChatParticipantsInfo, Long> {
    fun findAllByUser(user: User): List<ChatParticipantsInfo>?

}