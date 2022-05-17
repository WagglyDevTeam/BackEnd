package team.waggly.backend.security.jwt

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class JwtPreProcessingToken(
        principal: String, credentials: Int
) : UsernamePasswordAuthenticationToken(principal, credentials)