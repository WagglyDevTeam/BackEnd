package team.waggly.backend.dto.chatroomdto

import team.waggly.backend.model.ChatParticipantsInfo

data class ChatRoomResponseDto(
    val chatRoomId: Long?,
    val opponentNickname: String,
    val profileImage: String,
    //val chatLastMsg: String,
    //val chatLastMsgTime: LocalDateTime,
    val isAlarmOn: Boolean,
    //val unreadMsgCnt: Int
){
    constructor(chatInfo: ChatParticipantsInfo): this(
        chatRoomId = chatInfo.chatRoom.id,
        opponentNickname = chatInfo.opponent.nickName,
        profileImage = chatInfo.opponent.profileImgUrl,
        isAlarmOn = chatInfo.isAlarmOn
    )
}