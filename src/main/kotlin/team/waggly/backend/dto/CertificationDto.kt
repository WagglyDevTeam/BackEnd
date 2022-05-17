package team.waggly.backend.dto

class CertificationDto {
    data class Reqeust(
        val email: String,
        val certiNum: String
    )
    data class Response(
        val success: Boolean,
        val university: String
    )
}