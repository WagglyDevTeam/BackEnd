package team.waggly.backend.security.filter

import org.springframework.core.annotation.Order
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import team.waggly.backend.exception.security.JwtTokenInvalidException
import team.waggly.backend.security.jwt.JwtPreProcessingToken
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Order(4)
class JwtAuthFilter(
        requiresAuthenticationRequestMatcher: RequestMatcher
) : AbstractAuthenticationProcessingFilter(requiresAuthenticationRequestMatcher) {
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication? {
        val tokenPayload = request.getHeader("Authorization")
        if (tokenPayload.isNullOrEmpty()) {
            throw JwtTokenInvalidException("Not Found Jwt Token")
        }
        val principal = tokenPayload.substring(
                "Bearer ".length,
                tokenPayload.length
        )

        val jwtToken = JwtPreProcessingToken(principal, principal.length)
        return super.getAuthenticationManager().authenticate(jwtToken)
    }

    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain, authResult: Authentication?) {
        val context = SecurityContextHolder.createEmptyContext()

        context.authentication = authResult
        SecurityContextHolder.setContext(context)

        chain.doFilter(
                request,
                response
        )
    }

    override fun unsuccessfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, failed: AuthenticationException?) {
        SecurityContextHolder.clearContext()

        super.unsuccessfulAuthentication(request, response, failed)
    }
}