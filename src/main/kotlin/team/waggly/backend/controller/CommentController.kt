package team.waggly.backend.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import team.waggly.backend.dto.commentdto.CommentRequestDto
import team.waggly.backend.service.CommentService

@RestController
class CommentController(private val commentService: CommentService) {
    //댓글 작성
    @PostMapping("comment/{postId}")
    fun createComment(
        @PathVariable postId: Long,
        @RequestBody commentRequestDto: CommentRequestDto,
        @AuthenticationPrincipal userDetailImpl: UserDetailImpl?
    ): ResponseEntity<Any> {
        commentService.createComment(postId, commentRequestDto, userDetailImpl)
        return ResponseEntity
            .ok()
            .body(true)
    }

    //댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @AuthenticationPrincipal userDetailImpl: UserDetailImpl?
    ): ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(commentService.deleteComment(commentId, userDetailImpl))
    }
}