package team.waggly.backend.dto

class SignupDto {
    data class Request(
        val email: String,
        val password: String,
        val nickname: String,
        val university: String,
        val classNumber: Int,
        val major: String,
        val gender: String
    )
    data class Response(
        val success: Boolean
    )
}