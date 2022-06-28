package team.waggly.backend.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.dto.postDto.*
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.exception.CustomException
import team.waggly.backend.model.User
import team.waggly.backend.repository.PostLikeRepository
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.PostService
import team.waggly.backend.service.tika.TikaService
import javax.validation.Valid

@RestController
@RequestMapping("/board")
class PostController (
        private val postService: PostService,
        private val tikaService: TikaService,
) {
    // 모든 게시글 (학부 필터링 포함)
    @GetMapping
    fun getAllPosts(@PageableDefault(size = 10, page = 0) pageable: Pageable,
                    @RequestParam college: String?,
                    @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl?): ResponseDto<Any> {
        val user: User? = userDetailsImpl?.user ?: null
        if (college == null) {
            return ResponseDto(postService.getAllPosts(pageable, user))
        } else {
            val collegeEnum = when (college) {
                "test" -> CollegeType.TEST
                "artsports" -> CollegeType.ARTSPORTS
                "engineering" -> CollegeType.ENGINEERING
                "medical" -> CollegeType.MEDICAL
                "nature" -> CollegeType.NATURE
                "social" -> CollegeType.SOCIAL
                else -> throw NoSuchElementException("올바른 학부를 선택해주세요.")
            }
            return ResponseDto(postService.getAllCollegePosts(collegeEnum, pageable, user), HttpStatus.OK.value())
        }
    }

    // 게시글 상세
    @GetMapping("/{boardId}")
    fun getPostDetails(@PathVariable boardId: Long,
                       @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl?):
            ResponseDto<PostDetailResponseDto> {
        val user: User? = userDetailsImpl?.user ?: null
        return ResponseDto(postService.getPostDetails(boardId, user), HttpStatus.OK.value())
    }

    // 게시글 작성
    @PostMapping
    fun createPost(@AuthenticationPrincipal  userDetailsImpl: UserDetailsImpl,
                   @Valid @ModelAttribute postCreateDto: CreatePostRequestDto,
                   bindingResult: BindingResult): ResponseDto<Any> {
        if (bindingResult.hasErrors()) {
            val msg: MutableList<String> = arrayListOf()
            bindingResult.allErrors.forEach {
                val field = it as FieldError
                val message = it.defaultMessage
                msg.add("${field.field} : $message")
            }
            val result = bindingResult.allErrors.map {
                    error -> CustomException.ValidatorExceptionReturnType(error.code!!, error.defaultMessage!!)
            }
            return ResponseDto(CustomException.ValidatorException(result))
        }
        return ResponseDto(postService.createPost(postCreateDto, userDetailsImpl), HttpStatus.CREATED.value())
    }

    // 게시글 수정
    @PutMapping("/{boardId}")
    fun updatePost(@AuthenticationPrincipal  userDetailsImpl: UserDetailsImpl,
                   @PathVariable boardId: Long,
                   @Valid @ModelAttribute postUpdateDto: UpdatePostRequestDto,
                   bindingResult: BindingResult): ResponseDto<Any> {
        if (bindingResult.hasErrors()) {
            val msg: MutableList<String> = arrayListOf()
            bindingResult.allErrors.forEach {
                val field = it as FieldError
                val message = it.defaultMessage
                msg.add("${field.field} : $message")
            }
            val result = bindingResult.allErrors.map { error ->
                CustomException.ValidatorExceptionReturnType(error.code!!, error.defaultMessage!!)
            }
            return ResponseDto(CustomException.ValidatorException(result), "Validation Error", HttpStatus.UNPROCESSABLE_ENTITY.value())
        }
        return ResponseDto(postService.updatePost(boardId, postUpdateDto, userDetailsImpl), HttpStatus.OK.value())
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    fun deletePost(@PathVariable boardId: Long,
                   @AuthenticationPrincipal  userDetailsImpl: UserDetailsImpl): ResponseDto<DeletePostResponseDto> {
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

    // 파일 타입 체크
    @PostMapping("/fileType")
    fun fileTypeCheck(@ModelAttribute file: MultipartFile) {
        tikaService.mimeType(file)
    }
}