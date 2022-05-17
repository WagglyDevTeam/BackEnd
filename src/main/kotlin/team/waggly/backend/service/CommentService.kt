package team.waggly.backend.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.dto.commentdto.CommentRequestDto
import team.waggly.backend.model.Comment
import team.waggly.backend.repository.CommentRepository
import team.waggly.backend.repository.PostRepository

@Service
class CommentService(
    val commentRepository: CommentRepository,
    val postRepository: PostRepository
) {
    fun createComment(
        postId: Long,
        commentRequestDto: CommentRequestDto,
        userDetails: UserDetailsImpl
    ) {
        val post = postRepository.findByIdOrNull(postId) ?: throw IllegalArgumentException("해당 게시글이 없습니다.")
        val user = userDetails.user
        val comment = Comment(
            post = post,
            user = user,
            description = commentRequestDto.commentDesc
        )
        commentRepository.save(comment)
    }

    fun deleteComment(
        commentId: Long,
        userDetails: UserDetailsImpl
    ) {
        var comment = commentRepository.findByIdOrNull(commentId) ?: throw IllegalArgumentException("해당 댓글이 없습니다.")
        val user = userDetails.user
        if (comment.user != user) {
            throw Exception("댓글 작성자만 댓글을 삭제할 수 있습니다.")
        }
        comment = Comment(
            post = comment.post,
            user = comment.user,
            description = comment.description,
            activeStatus = ActiveStatusType.INACTIVE
        )
        commentRepository.save(comment)
    }
}