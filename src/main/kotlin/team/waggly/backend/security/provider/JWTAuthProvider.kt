package team.waggly.backend.security.provider

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import team.waggly.backend.exception.security.JwtTokenInvalidException
import team.waggly.backend.repository.UserRepository
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.security.jwt.JwtDecoder
import team.waggly.backend.security.jwt.JwtPreProcessingToken
import team.waggly.backend.security.jwt.JwtTokenUtils

class JWTAuthProvider(
        private val userRepository: UserRepository,
        private val jwtTokenUtils: JwtTokenUtils,
) : AuthenticationProvider {
    private val jwtDecoder = JwtDecoder(jwtTokenUtils)

    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication.principal as String
        val username = jwtDecoder.decodeUsername(token)

        val user = userRepository.findByEmail(username) ?: throw JwtTokenInvalidException("Not Found User")
        val userDetails = UserDetailsImpl(user)

        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication.isAssignableFrom(JwtPreProcessingToken::class.java)
    }
}