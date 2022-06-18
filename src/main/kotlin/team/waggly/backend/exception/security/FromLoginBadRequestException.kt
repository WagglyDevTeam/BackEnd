package team.waggly.backend.exception.security

import org.springframework.http.HttpStatus

class FromLoginBadRequestException : RuntimeException {
    open val status = HttpStatus.INTERNAL_SERVER_ERROR

    constructor(message: String? = "Bad Request Login Info") : super(message)
}