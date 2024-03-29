package team.waggly.backend.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import team.waggly.backend.dto.PagingResponseDto
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.post.*
import team.waggly.backend.exception.ErrorMessage
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
    fun getPostsInHome(@RequestParam userId: Long? = null): ResponseDto<PostsInHomeResponseDto> {
        return postService.getPostsInHome(userId)
    }

    // 모든 게시글 (학부 필터링 포함)
    @GetMapping
    fun searchPostsByCollege(
            searchPostsByCollege: SearchPostsByCollege,
            @PageableDefault(size = 10, page = 0) pageable: Pageable?,
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl
    ): PagingResponseDto<SearchPostsByCollegeResponseDto> {
        searchPostsByCollege.pageable = pageable
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
            @Valid @ModelAttribute postCreateDto: CreatePostRequestDto,
            bindingResult: BindingResult
    ): ResponseDto<CreatePostResponseDto> {
        val user = userDetailsImpl.user
        val error = ErrorMessage(bindingResult).getError()
        if(error.isError){
            throw Exception(error.errorMsg)
        }

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
            @Valid @ModelAttribute postUpdateDto: UpdatePostRequestDto,
            bindingResult: BindingResult
    ): ResponseDto<Any> {
        val user = userDetailsImpl.user
        val error = ErrorMessage(bindingResult).getError()
        if(error.isError){
            throw Exception(error.errorMsg)
        }
        return ResponseDto(
                postService.updatePost(boardId, postUpdateDto, user),
                HttpStatus.OK.value()
        )
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    fun deletePost(
            @PathVariable boardId: Long,
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl
    ): ResponseDto<DeletePostResponseDto> {
        val user = userDetailsImpl.user
        return ResponseDto(
                postService.deletePost(boardId, user),
                HttpStatus.NO_CONTENT.value()
        )
    }

    // 게시글 좋아요
    @PutMapping("/{boardId}/like")
    fun likePost(
            @PathVariable boardId: Long,
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl
    ): ResponseDto<PostLikeResponseDto> {
        val userId = userDetailsImpl.user.id!!
        return ResponseDto(
                postService.likePost(boardId, userId),
                HttpStatus.OK.value()
        )
    }

    @GetMapping("/search")
    fun searchPost(
        searchPostRequest: SearchPostRequest,
        @PageableDefault(size = 10, page = 0) pageable: Pageable?,
        @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
    ): ResponseDto<SearchPostResponse> {
        searchPostRequest.pageable = pageable
        return ResponseDto(
                postService.searchPost(searchPostRequest, userDetailsImpl.user.id!!)
        )
    }
}