package team.waggly.backend.exception.security

import org.springframework.http.HttpStatus

class FromLoginInvalidException : RuntimeException {
    open val status = HttpStatus.INTERNAL_SERVER_ERROR

    constructor(message: String? = "Invalid Login") : super(message)
}