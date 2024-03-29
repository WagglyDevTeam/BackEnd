package team.waggly.backend.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.commentdto.CommentLikeResponseDto
import team.waggly.backend.dto.commentdto.CommentRequestDto
import team.waggly.backend.dto.commentdto.ReplyRequestDto
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.CommentService

@RestController
class CommentController(private val commentService: CommentService) {
    //댓글 작성
    @PostMapping("comment/{postId}")
    fun createComment(
        @PathVariable postId: Long,
        @RequestBody commentRequestDto: CommentRequestDto,
        @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl // null 불가능
    ): ResponseDto<Any> {
        return ResponseDto(commentService.createComment(postId, commentRequestDto, userDetailsImpl))
    }

    //댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @AuthenticationPrincipal userDetailImpl: UserDetailsImpl
    ): ResponseDto<Any> {
        commentService.deleteComment(commentId, userDetailImpl)
        return ResponseDto()
    }

    //댓글 좋아요
    @PutMapping("/like/comment/{commentId}")
    fun likeComment(
        @PathVariable commentId: Long,
        @AuthenticationPrincipal userDetailImpl: UserDetailsImpl
    ): ResponseDto<CommentLikeResponseDto> {
        return ResponseDto(commentService.likeComment(commentId, userDetailImpl))
    }

    //대댓글 작성
    @PostMapping("reply/{commentId}")
    fun createReply(
        @PathVariable commentId: Long,
        @RequestBody replyRequestDto: ReplyRequestDto,
        @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl // null 불가능
    ): ResponseDto<Any> {
        return ResponseDto(commentService.createReply(commentId, replyRequestDto, userDetailsImpl))
    }
}