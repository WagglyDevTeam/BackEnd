package team.waggly.backend.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.waggly.backend.dto.commentdto.CommentRequestDto
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
    ): ResponseEntity<Any> {

        commentService.createComment(postId, commentRequestDto, userDetailsImpl)

        return ResponseEntity
            .ok()
            .body(true)
    }

    //댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @AuthenticationPrincipal userDetailImpl: UserDetailsImpl
    ): ResponseEntity<Any> {

        commentService.deleteComment(commentId, userDetailImpl)

        return ResponseEntity
            .ok()
            .body(true)
    }

    //댓글 좋아요
    @PostMapping("/like/comment/{commentId}")
    fun likeComment(
        @PathVariable commentId: Long,
        @AuthenticationPrincipal userDetailImpl: UserDetailsImpl
    ): ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(commentService.likeComment(commentId, userDetailImpl))
    }
}