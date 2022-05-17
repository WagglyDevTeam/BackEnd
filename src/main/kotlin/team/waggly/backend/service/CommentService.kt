package team.waggly.backend.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.dto.commentdto.CommentLikeResponseDto
import team.waggly.backend.dto.commentdto.CommentRequestDto
import team.waggly.backend.model.Comment
import team.waggly.backend.model.CommentLike
import team.waggly.backend.repository.CommentLikeRepository
import team.waggly.backend.repository.CommentRepository
import team.waggly.backend.repository.PostRepository
import team.waggly.backend.security.UserDetailsImpl
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
            description = commentRequestDto.commentDesc
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
        if (comment.user != user) {
            throw Exception("댓글 작성자만 댓글을 삭제할 수 있습니다.")
        }
        comment = Comment(
            post = comment.post,
            user = comment.user,
            description = comment.description,
            activeStatus = ActiveStatusType.INACTIVE
            //deleteTime 필요?
        )
        commentRepository.save(comment)
    }

    fun likeComment(
        commentId: Long,
        userDetailImpl: UserDetailsImpl
    ): CommentLikeResponseDto {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw IllegalArgumentException("해당 댓글이 없습니다.")
        val user = userDetailImpl.user
        val commentLike: CommentLike =
            commentLikeRepository.findByUserIdAndCommentIdOrNull(user.id!!, commentId) //null 처리 해야할듯

        //commentLike 가 존재하지 않는다면 commentLIkeRepository.save 후 dto 생성 및 반환
        //commentLike 가 존재한다면 commentLikeRepository.delete 후 dto 생성 및 반환

        //좋아요 취소
        if (commentLikeRepository.existsByUserIdAndCommentId(user.id, commentId)) {
            commentLike.id?.let { commentLikeRepository.deleteById(it) } // null 체크?

            //함수로 만들어야 함
            return CommentLikeResponseDto(
                isLikedByMe = false,
                commentLikeCnt = commentLikeRepository.countByCommentId(commentId)
            )
            //
        } else {
            val newCommentLike = CommentLike(
                comment = comment,
                userId = user.id
            )
            commentLikeRepository.save(newCommentLike)
            //
            return CommentLikeResponseDto(
                isLikedByMe = false,
                commentLikeCnt = commentLikeRepository.countByCommentId(commentId)
            )
            //
        }
    }
}