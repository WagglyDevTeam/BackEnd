package team.waggly.backend.security.filter

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import team.waggly.backend.exception.security.FromLoginBadRequestException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Order(3)
class FormLoginFilter : UsernamePasswordAuthenticationFilter {
    private val objectMapper: ObjectMapper

    constructor(authenticationManager: AuthenticationManager) {
        super.setAuthenticationManager(authenticationManager)
        objectMapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication? {
        val authRequest: UsernamePasswordAuthenticationToken
        try {
            val requestBody = objectMapper.readTree(request.inputStream)
            val username = requestBody.get("username").asText()
            val password = requestBody.get("password").asText()
            authRequest = UsernamePasswordAuthenticationToken(username, password)
        } catch (e: Exception) {
            throw FromLoginBadRequestException("username, password 입력이 필요합니다.")
        }

        setDetails(request, authRequest)
        return this.authenticationManager.authenticate(authRequest)
    }
}