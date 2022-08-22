package team.waggly.backend.controller

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.*
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.post.*
import team.waggly.backend.exception.CustomException
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.PostService
import javax.validation.Valid

@RestController
@RequestMapping("/board")
class PostController(
        private val postService: PostService,
) {
    // 게시판 홈 (로그인, 비로그인)
    @GetMapping("/home")
    fun getPostsInHome(@AuthenticationPrincipal userDetailsImpl: UserDetailsImpl?): ResponseDto<PostsInHomeResponseDto> {
        return postService.getPostsInHome(userDetailsImpl?.user)
    }

    // 모든 게시글 (학부 필터링 포함)
    @GetMapping
    fun searchPostsByCollege(
            @RequestParam searchPostsByCollege: SearchPostsByCollege,
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl
    ): ResponseDto<SearchPostsByCollegeResponseDto> {
        searchPostsByCollege.user = userDetailsImpl.user
        return postService.searchPostsByCollege(searchPostsByCollege)
    }

    // 게시글 상세
    @GetMapping("/{boardId}")
    fun getPostDetails(@PathVariable boardId: Long,
                       @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl
    ): ResponseDto<PostDetailResponseDto> {
        val user = userDetailsImpl.user
        return ResponseDto(postService.getPostDetails(boardId, user), HttpStatus.OK.value())
    }

    // 게시글 작성
    @PostMapping
    fun createPost(
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
            @Valid @RequestBody postCreateDto: CreatePostRequestDto,
    ): ResponseDto<Any> {
        val user = userDetailsImpl.user
        return ResponseDto(
                postService.createPost(postCreateDto, user),
                HttpStatus.CREATED.value()
        )
    }

    // 게시글 수정
    @PutMapping("/{boardId}")
    fun updatePost(
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
            @PathVariable boardId: Long,
            @Valid @RequestBody postUpdateDto: UpdatePostRequestDto,
    ): ResponseDto<Any> {
        val user = userDetailsImpl.user
        return ResponseDto(
                postService.updatePost(boardId, postUpdateDto, user),
                HttpStatus.OK.value()
        )
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    fun deletePost(@PathVariable boardId: Long,
                   @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl): ResponseDto<DeletePostResponseDto> {
        postService.deletePost(boardId, userDetailsImpl.user)
        return ResponseDto(DeletePostResponseDto(true), HttpStatus.NO_CONTENT.value())
    }

    // 게시글 좋아요
    @PutMapping("/{boardId}/like")
    fun likePost(@PathVariable boardId: Long,
                 @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl): ResponseDto<PostLikeResponseDto> {
        val userId: Long = userDetailsImpl.user.id ?: throw NoSuchElementException()
        return ResponseDto(postService.likePost(boardId, userId), HttpStatus.OK.value())
    }
}