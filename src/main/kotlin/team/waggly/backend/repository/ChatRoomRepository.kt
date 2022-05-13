package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.waggly.backend.model.ChatRoom

interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {
}