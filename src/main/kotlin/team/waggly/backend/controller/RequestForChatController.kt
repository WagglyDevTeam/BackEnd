package team.waggly.backend.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.chat.CreateRequestForChatRequest
import team.waggly.backend.dto.chat.CreateRequestForChatResponse
import team.waggly.backend.dto.chat.GetRequestForChatRecommendRequest
import team.waggly.backend.dto.chat.GetRequestForChatRecommendResponse
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.RequestForChatService

@RestController
@RequestMapping("/request/chat")
class RequestForChatController(
        private val requestForChatService: RequestForChatService,
) {
    @GetMapping
    fun getRequestForChatRecommend(
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
            @RequestBody getRequestForChatRecommendRequest: GetRequestForChatRecommendRequest,
    ): ResponseDto<List<GetRequestForChatRecommendResponse>> {
        return ResponseDto(
                requestForChatService.getRequestForChatRecommend(getRequestForChatRecommendRequest, userDetailsImpl.user)
        )
    }

    @PostMapping
    fun createRequestForChat(
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
            @RequestBody createRequestForChatRequest: CreateRequestForChatRequest,
    ): ResponseDto<CreateRequestForChatResponse> {
        return ResponseDto(
                requestForChatService.createRequestForChat(createRequestForChatRequest, userDetailsImpl.user)
        )
    }
}