package team.waggly.backend.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.dto.chat.CreateRequestForChatRequest
import team.waggly.backend.dto.chat.CreateRequestForChatResponse
import team.waggly.backend.dto.chat.GetRequestForChatRecommendRequest
import team.waggly.backend.dto.chat.GetRequestForChatRecommendResponse
import team.waggly.backend.model.RequestForChat
import team.waggly.backend.model.User
import team.waggly.backend.repository.RequestForChatRepository
import team.waggly.backend.repository.UserRepository

@Service
class RequestForChatService(
        private val requestForChatRepository: RequestForChatRepository,
        private val userRepository: UserRepository,
) {
    fun getRequestForChatRecommend(requestBody: GetRequestForChatRecommendRequest, user: User): List<GetRequestForChatRecommendResponse> {
        // 알림 설정에 따라 추천자 다르게 내리는 것 필요
        val users = when(requestBody.chatClass) {
            RequestForChat.ChatClassType.JUNIOR -> {
                userRepository.findAllByClassNumGreaterThan(user.classNum)
            }
            RequestForChat.ChatClassType.MATE -> {
                userRepository.findAllByClassNum(user.classNum)
            }
            RequestForChat.ChatClassType.SENIOR -> {
                userRepository.findAllByClassNumIsLessThan(user.classNum)
            }
        }

        return users.filterIndexed { index, _ -> index < 3  }.map { GetRequestForChatRecommendResponse(it) }
    }

    fun createRequestForChat(requestBody: CreateRequestForChatRequest, user: User): CreateRequestForChatResponse {
        val receiver = userRepository.findByIdOrNull(requestBody.receiverId)
                ?: throw Exception("회원정보가 없습니다.")

        val requestForChat = requestForChatRepository.save(
                RequestForChat(
                        sender = user,
                        receiver = receiver,
                        chatPurpose = requestBody.chatPurpose,
                        chatClass = requestBody.chatClass,
                        requestMsg = requestBody.requestMsg,
                )
        )

        return CreateRequestForChatResponse(requestForChatId = requestForChat.id!!)
    }
}