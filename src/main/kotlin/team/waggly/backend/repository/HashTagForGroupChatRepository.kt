package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
<<<<<<< HEAD
import org.springframework.stereotype.Repository
import team.waggly.backend.model.HashTagForGroupChat

@Repository
=======
import team.waggly.backend.model.HashTagForGroupChat

>>>>>>> 17a95112e49562fb52eeb4f8841ed553a3880a4b
interface HashTagForGroupChatRepository : JpaRepository<HashTagForGroupChat, Long> {
}