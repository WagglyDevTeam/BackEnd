package team.waggly.backend.dto.chat

import team.waggly.backend.commomenum.ChatPurposeType
import team.waggly.backend.model.RequestForChat

class CreateRequestForChatRequest(
        val requestMsg: String,
        val chatPurpose: ChatPurposeType,
        val chatClass: RequestForChat.ChatClassType,
) {
}