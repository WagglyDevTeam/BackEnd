package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.model.ChatRoom

@Repository
interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {
}