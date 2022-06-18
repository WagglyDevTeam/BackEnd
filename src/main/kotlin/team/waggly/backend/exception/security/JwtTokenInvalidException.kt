package team.waggly.backend.exception.security

import org.springframework.http.HttpStatus

class JwtTokenInvalidException : RuntimeException {
    open val status = HttpStatus.INTERNAL_SERVER_ERROR

    constructor(message: String? = "Invalid Token") : super(message)
}