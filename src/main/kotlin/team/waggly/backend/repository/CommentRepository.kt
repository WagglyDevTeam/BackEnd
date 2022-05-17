package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
<<<<<<< HEAD
import org.springframework.stereotype.Repository
import team.waggly.backend.model.Comment

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    fun countByPostId(postId: Long): Int
=======
import team.waggly.backend.model.Comment


interface CommentRepository : JpaRepository<Comment, Long> {
>>>>>>> 17a95112e49562fb52eeb4f8841ed553a3880a4b
}