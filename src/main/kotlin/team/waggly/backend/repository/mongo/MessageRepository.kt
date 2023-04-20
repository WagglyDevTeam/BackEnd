package team.waggly.backend.repository.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.model.mongo.Message

interface MessageRepository : MongoRepository<Message?, Long?>{

}