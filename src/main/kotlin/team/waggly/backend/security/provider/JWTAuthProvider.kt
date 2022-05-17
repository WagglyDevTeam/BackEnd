package team.waggly.backend.security.provider

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException
import team.waggly.backend.repository.UserRepository
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.security.jwt.JwtDecoder
import team.waggly.backend.security.jwt.JwtPreProcessingToken
import java.lang.IllegalArgumentException

class JWTAuthProvider(private val userRepository: UserRepository): AuthenticationProvider {
    private val jwtDecoder = JwtDecoder()

    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication.principal as String
        val username = jwtDecoder.decodeUsername(token)

        val user = userRepository.findByEmail(username)?: throw IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        val userDetails = UserDetailsImpl(user)

        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication.isAssignableFrom(JwtPreProcessingToken::class.java)
    }
}