package team.waggly.backend.dto

class SendEmailDto {
    data class Request(
        val email: String
    )
    data class Response(
        val success: Boolean
    )
}