package team.waggly.backend.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.dto.commentdto.CommentLikeResponseDto
import team.waggly.backend.dto.commentdto.CommentRequestDto
import team.waggly.backend.dto.commentdto.ReplyRequestDto
import team.waggly.backend.model.Comment
import team.waggly.backend.model.CommentLike
import team.waggly.backend.repository.CommentLikeRepository
import team.waggly.backend.repository.CommentRepository
import team.waggly.backend.repository.PostRepository
import team.waggly.backend.security.UserDetailsImpl
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class CommentService(
    val commentRepository: CommentRepository,
    val commentLikeRepository: CommentLikeRepository,
    val postRepository: PostRepository
) {

    //댓글 작성
    @Transactional
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
            description = commentRequestDto.commentDesc,
            isAnonymous = commentRequestDto.isAnonymous
        )
        commentRepository.save(comment)
    }

    //댓글 삭제
    @Transactional
    fun deleteComment(
        commentId: Long,
        userDetails: UserDetailsImpl
    ) {
        var comment = commentRepository.findByIdOrNull(commentId) ?: throw IllegalArgumentException("해당 댓글이 없습니다.")
        val user = userDetails.user
        if (comment.user.id != user.id) {
            throw Exception("댓글 작성자만 댓글을 삭제할 수 있습니다.")
        }
        comment.deletedAt = LocalDateTime.now()
        comment.activeStatus = ActiveStatusType.INACTIVE
        commentRepository.save(comment)
    }

    fun likeComment(
        commentId: Long,
        userDetailImpl: UserDetailsImpl
    ): CommentLikeResponseDto {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw IllegalArgumentException("해당 댓글이 없습니다.")
        val user = userDetailImpl.user
        val commentLike: CommentLike? = commentLikeRepository.findByUserIdAndCommentId(user.id!!, commentId)
        var isLikedByMe = false

        // 처음 좋아요 누를 때
        if (commentLike == null) {
            val newCommentLike = CommentLike(
                comment = comment,
                userId = user.id
            )
            commentLikeRepository.save(newCommentLike)
            isLikedByMe = true
        } else {
            when (commentLike.activeStatus) {
                //좋아요 취소
                ActiveStatusType.ACTIVE -> {
                    commentLike.activeStatus = ActiveStatusType.INACTIVE
                    isLikedByMe = false
                }
                //좋아요 다시 누르기
                ActiveStatusType.INACTIVE -> {
                    commentLike.activeStatus = ActiveStatusType.ACTIVE
                    isLikedByMe = true
                }
            }
            commentLikeRepository.save(commentLike)
        }
        val commentLikeCnt: Int =
            commentLikeRepository.countByCommentIdAndActiveStatus(commentId, ActiveStatusType.ACTIVE)
        return CommentLikeResponseDto(
            isLikedByMe,
            commentLikeCnt
        )
    }

    //대댓글 생성
    @Transactional
    fun createReply(commentId: Long, replyRequestDto: ReplyRequestDto, userDetails: UserDetailsImpl) {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw IllegalArgumentException("해당 댓글이 없습니다.")
        if(comment.parentComment != null){
            throw Exception("1계층 대댓글만 가능합니다.")
        }
        val user = userDetails.user
        val reply = Comment(
            post = comment.post,
            user = user,
            description = replyRequestDto.replyDesc,
            isAnonymous = replyRequestDto.isAnonymous,
            parentComment = comment
        )
        commentRepository.save(reply)
    }


}