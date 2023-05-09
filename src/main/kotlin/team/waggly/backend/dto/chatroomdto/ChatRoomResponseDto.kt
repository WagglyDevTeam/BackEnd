package team.waggly.backend.dto.chatroomdto

import com.fasterxml.jackson.annotation.JsonProperty
import team.waggly.backend.model.ChatParticipantsInfo

data class ChatRoomResponseDto(
    val chatRoomId: Long?,
    val opponentNickname: String,
    val profileImage: String,
    //val chatLastMsg: String,
    //val chatLastMsgTime: LocalDateTime,
    @get:JsonProperty("isAlarmOn")
    val isAlarmOn: Boolean,
    val isBindOn: Boolean
    //val unreadMsgCnt: Int
){
    constructor(chatInfo: ChatParticipantsInfo): this(
        chatRoomId = chatInfo.chatRoom.id,
        opponentNickname = chatInfo.opponent.nickName,
        profileImage = chatInfo.opponent.profileImgUrl,
        isAlarmOn = chatInfo.isAlarmOn,
        isBindOn = chatInfo.isBindOn
    )
}