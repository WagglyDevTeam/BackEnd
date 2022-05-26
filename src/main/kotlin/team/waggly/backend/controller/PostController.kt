package team.waggly.backend.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.*
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.dto.postDto.*
import team.waggly.backend.exception.CustomException
import team.waggly.backend.model.User
import team.waggly.backend.repository.PostLikeRepository
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.PostService
import javax.validation.Valid

@RestController
class PostController (
        private val postService: PostService,
        private val postLikeRepository: PostLikeRepository
) {
    // 모든 게시글 (학부 필터링 포함)
    @GetMapping("/board")
    fun getAllPosts(@PageableDefault(size = 10, page = 0) pageable: Pageable,
                    @RequestParam college: String?,
                    @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl?): ResponseEntity<Any> {
        val user: User? = userDetailsImpl?.user ?: null
        if (college == null) {
            return ResponseEntity.ok().body(postService.getAllPosts(pageable, user))
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

            return ResponseEntity.ok().body(postService.getAllPostsByCollegeByOrderByIdDesc(collegeEnum, pageable, user))
        }
    }

    // 게시글 상세
    @GetMapping("/board/{boardId}")
    fun getPostDetails(@PathVariable boardId: Long,
                       @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl?):
            ResponseEntity<PostDetailDto> {
        val user: User? = userDetailsImpl?.user ?: null
        return ResponseEntity.ok().body(postService.getPostDetails(boardId, user))
    }

    // 게시글 작성
    @PostMapping("/board")
    fun createPost(@AuthenticationPrincipal  userDetailsImpl: UserDetailsImpl,
                   @Valid @ModelAttribute postCreateDto: CreatePostRequestDto,
                   bindingResult: BindingResult): ResponseEntity<Any> {
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
            return ResponseEntity.badRequest().body(CustomException.ValidatorException(result))
        }
        return ResponseEntity<Any>(postService.createPost(postCreateDto, userDetailsImpl), HttpStatus.CREATED)
    }

    // 게시글 수정
    @PutMapping("/board/{boardId}")
    fun updatePost(@AuthenticationPrincipal  userDetailsImpl: UserDetailsImpl,
                   @PathVariable boardId: Long,
                   @Valid @ModelAttribute postUpdateDto: UpdatePostRequestDto,
                   bindingResult: BindingResult): ResponseEntity<Any> {
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
            return ResponseEntity.badRequest().body(CustomException.ValidatorException(result))
        }
        return ResponseEntity.ok().body(postService.updatePost(boardId, postUpdateDto, userDetailsImpl))
    }

    // 게시글 삭제
    @DeleteMapping("/board/{boardId}")
    fun deletePost(@PathVariable boardId: Long,
                   @AuthenticationPrincipal  userDetailsImpl: UserDetailsImpl): ResponseEntity<Any> {
        postService.deletePost(boardId, userDetailsImpl.user)
        return ResponseEntity<Any>(DeletePostResponseDto(true), HttpStatus.NO_CONTENT)
    }

    // 게시글 좋아요
    @PutMapping("/board/{boardId}/like")
    fun likePost(@PathVariable boardId: Long,
                 @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl): ResponseEntity<PostLikeResponseDto> {
        val userId: Long = userDetailsImpl.user.id ?: throw NoSuchElementException()
        return ResponseEntity.ok().body(postService.likePost(boardId, userId))
    }
}


//
//    @GetMapping("/post/{college}")
//    fun getAllPostsByCollege(@RequestParam college: String?,
//                             @PageableDefault(size = 10, page = 0) pageable: Pageable,
//                             @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl): PostDto.CollegePostsResponseDto {
//        if (college == null) {
//            throw IllegalArgumentException("해당하는 학부가 없습니다.")
//        }
//        val collegeEnum = when (college) {
//            "test" -> CollegeType.TEST
//            "artsports" -> CollegeType.ARTSPORTS
//            "engineering" -> CollegeType.ENGINEERING
//            "medical" -> CollegeType.MEDICAL
//            "nature" -> CollegeType.NATURE
//            "social" -> CollegeType.SOCIAL
//            else -> throw java.lang.IllegalArgumentException("올바른 학부를 선택해주세요.")
//        }
//
//        val user: User = userDetailsImpl.user
//        return postService.getAllPostsByCollegeByOrderByIdDesc(collegeEnum, pageable, user)
//    }