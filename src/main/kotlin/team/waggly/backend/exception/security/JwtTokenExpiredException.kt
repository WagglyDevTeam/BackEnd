package team.waggly.backend.exception.security

import org.springframework.http.HttpStatus

class JwtTokenExpiredException : RuntimeException {
    open val status = HttpStatus.INTERNAL_SERVER_ERROR

    constructor() : super("Expired Token")
}