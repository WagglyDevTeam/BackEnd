package team.waggly.backend.repository.mongo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository
import team.waggly.backend.model.mongo.Message

@Repository
interface MessageRepository : MongoRepository<Message, String>{
    fun findAllByRoomId(roomId: Long, pageable: Pageable) : Page<Message>

    fun findAllByType(type: String) : List<Message>
}