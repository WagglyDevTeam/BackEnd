package team.waggly.backend.security.provider

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import team.waggly.backend.security.UserDetailsImpl
import javax.annotation.Resource

class FormLoginAuthProvider(
        @Resource(name = "userDetailsServiceImpl") private val userDetailsService: UserDetailsService,
        private val passwordEncoder: BCryptPasswordEncoder)
    : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication as UsernamePasswordAuthenticationToken

        val username = token.name
        val password = token.credentials as String

        val userDetails = userDetailsService.loadUserByUsername(username) as UserDetailsImpl
        if (!passwordEncoder.matches(password, userDetails.password)) {
            throw BadCredentialsException(userDetails.username + "Invalid password")
        }

        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}