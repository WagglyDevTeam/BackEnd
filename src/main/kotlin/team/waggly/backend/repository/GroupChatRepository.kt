package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository

interface GroupChatRepository : JpaRepository<GroupChatRepository, Long> {
}