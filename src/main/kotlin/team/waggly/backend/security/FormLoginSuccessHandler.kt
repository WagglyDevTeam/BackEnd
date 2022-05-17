package team.waggly.backend.security

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import team.waggly.backend.security.jwt.JwtTokenUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class FormLoginSuccessHandler: SavedRequestAwareAuthenticationSuccessHandler() {
    val AUTH_HEADER = "Authorization"
    val TOKEN_TYPE = "BEARER"

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val userDetails = authentication.principal as UserDetailsImpl

        val token = JwtTokenUtils().generateJwtToken(userDetails)
        response.addHeader(AUTH_HEADER, "$TOKEN_TYPE $token")
    }
}