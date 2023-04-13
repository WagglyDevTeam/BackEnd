package team.waggly.backend.dto.chat

import team.waggly.backend.commomenum.ChatPurposeType
import team.waggly.backend.model.RequestForChat

class GetRequestForChatRecommendRequest(
        val chatPurpose: ChatPurposeType,
        val chatClass: RequestForChat.ChatClassType,
)