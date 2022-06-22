package team.waggly.backend.security.filter

import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import team.waggly.backend.exception.security.FromLoginBadRequestException
import team.waggly.backend.exception.security.FromLoginInvalidException
import team.waggly.backend.exception.security.JwtTokenExpiredException
import team.waggly.backend.exception.security.JwtTokenInvalidException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Order(2)
class ExceptionHandlerFilter : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: JwtTokenInvalidException) {
            response.status = HttpStatus.UNAUTHORIZED.value() // 401 ErrorCode
            response.writer.write(e.message ?: "Invalid Token")
        } catch (e: JwtTokenExpiredException) {
            response.status = HttpStatus.UNAUTHORIZED.value() // 401 ErrorCode
            response.writer.write(e.message ?: "Expired Token")
        } catch (e: FromLoginBadRequestException) {
            response.status = HttpStatus.BAD_REQUEST.value() // 401 ErrorCode
            response.writer.write(e.message ?: "Bad Request Login Info")
        } catch (e: FromLoginInvalidException) {
            response.status = HttpStatus.UNAUTHORIZED.value() // 401 ErrorCode
            response.writer.write(e.message ?: "Invalid Login")
        }
    }
}