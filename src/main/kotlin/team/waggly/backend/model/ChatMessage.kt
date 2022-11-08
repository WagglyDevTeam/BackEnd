package team.waggly.backend.model

data class ChatMessage (
     var type: MessageType,
     var content: String?,
     var sender: String
)

enum class MessageType {
    CHAT,
    JOIN,
    LEAVE
}