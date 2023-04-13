package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.model.User

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByNickName(nickname: String): User?

    fun existsByEmail(email: String): Boolean

    fun findAllByClassNum(classNum: Int): List<User>

    fun findAllByClassNumGreaterThan(classNum: Int): List<User>

    fun findAllByClassNumIsLessThan(classNum: Int): List<User>
}