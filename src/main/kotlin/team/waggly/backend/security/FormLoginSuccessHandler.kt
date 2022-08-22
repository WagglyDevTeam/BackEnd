package team.waggly.backend.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.web.bind.annotation.RequestMapping
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.user.UserLoginResponseDto
import team.waggly.backend.security.jwt.JwtTokenUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class FormLoginSuccessHandler(
        private val jwtTokenUtils: JwtTokenUtils,
): SavedRequestAwareAuthenticationSuccessHandler() {
    val AUTH_HEADER = "Authorization"
    val TOKEN_TYPE = "BEARER"

    val mapper = ObjectMapper()

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val userDetails = authentication.principal as UserDetailsImpl
        val token = jwtTokenUtils.generateJwtToken(userDetails)

        val responseDto = ResponseDto(UserLoginResponseDto(userDetails.user))
        response.writer.write(mapper.writeValueAsString(responseDto))
        response.addHeader(AUTH_HEADER, "$TOKEN_TYPE $token")
        response.contentType = "application/json"
    }
}