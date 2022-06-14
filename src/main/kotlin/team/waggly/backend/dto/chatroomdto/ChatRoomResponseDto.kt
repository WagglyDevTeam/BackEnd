package team.waggly.backend.dto.chatroomdto

import java.time.LocalDateTime

data class ChatRoomResponseDto(
    val chatRoomId: Int,
    val opponentNickname: String,
    val profileImage: String,
    val chatLastMsg: String,
    val chatLastMsgTime: LocalDateTime,
    val isAlarmOn: Boolean,
    val unreadMsgCnt: Int
)