package team.waggly.backend.exception

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import team.waggly.backend.dto.ErrorResponse
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class GlobalExceptionHandler {
    @ResponseBody
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(BindException::class)
    fun bindExceptionHandler(req: HttpServletRequest?, e: BindException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
                ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        e.bindingResult.fieldErrors.firstOrNull()?.let { fieldError ->
                            """${fieldError.field} 파라미터는 ${fieldError.defaultMessage}"""
                        } ?: "요청 파라미터를 확인해 주세요"
                ),
                HttpStatus.BAD_REQUEST
        )
    }

    @ResponseBody
    @ExceptionHandler(Exception::class)
    fun unknownExceptionHandler(req: HttpServletRequest, e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
                ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message),
                HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}