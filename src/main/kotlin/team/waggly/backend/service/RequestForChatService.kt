package team.waggly.backend.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ChatPurposeType
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.chat.CreateRequestForChatRequest
import team.waggly.backend.dto.chat.CreateRequestForChatResponse
import team.waggly.backend.model.RequestForChat
import team.waggly.backend.model.User
import team.waggly.backend.repository.RequestForChatRepository
import team.waggly.backend.repository.UserRepository

@Service
class RequestForChatService(
        private val requestForChatRepository: RequestForChatRepository,
        private val userRepository: UserRepository,
) {
    fun createRequestForChat(requestBody: CreateRequestForChatRequest, user: User): ResponseDto<CreateRequestForChatResponse> {
        val receiver = this.getRecommendUser(requestBody.chatPurpose, requestBody.chatClass)
        val requestForChat = requestForChatRepository.save(
                RequestForChat(
                        sender = user,
                        receiver = receiver,
                        chatPurpose = requestBody.chatPurpose,
                        chatClass = requestBody.chatClass,
                        requestMsg = requestBody.requestMsg
                )
        )
        return ResponseDto(CreateRequestForChatResponse(requestForChatId = requestForChat.id!!))
    }

    private fun getRecommendUser(chatPurpose: ChatPurposeType, chatClass: RequestForChat.ChatClassType): User {
        return userRepository.findByIdOrNull(1L)
                ?: throw Exception("회원정보가 없습니다.")
    }
}