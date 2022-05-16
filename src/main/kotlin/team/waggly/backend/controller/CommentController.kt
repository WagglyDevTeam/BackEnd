package team.waggly.backend.controller

import org.springframework.web.bind.annotation.*
import team.waggly.backend.dto.commentdto.CommentRequestDto
import team.waggly.backend.service.CommentService

@RestController
class CommentController(private val commentService: CommentService) {
    //댓글 작성
    @PostMapping("newcomment")
    fun createComment(@RequestBody commentRequestDto: CommentRequestDto) {
        return commentService.createComment(commentRequestDto)
    }

    //댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    fun deleteComment(@PathVariable commentId : Long) {
        return commentService.deleteComment(commentId)
    }
}