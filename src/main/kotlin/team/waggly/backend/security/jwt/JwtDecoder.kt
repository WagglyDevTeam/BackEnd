package team.waggly.backend.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.stereotype.Component
import team.waggly.backend.exception.security.JwtTokenExpiredException
import team.waggly.backend.exception.security.JwtTokenInvalidException
import java.util.*

@Component
class JwtDecoder(private val jwtTokenUtils: JwtTokenUtils) {
    fun decodeUsername(token: String): String {
        val decodedJWT = isValidToken(token)

        val expiredDate = decodedJWT
                .getClaim(jwtTokenUtils.CLAIM_EXPIRED_DATE)
                .asDate()

        val now = Date()
        if (expiredDate.before(now)) {
            throw JwtTokenExpiredException()
        }

        return decodedJWT
                .getClaim(jwtTokenUtils.CLAIM_USER_NAME)
                .asString()
    }

    fun isValidToken(token: String): DecodedJWT {
        try {
            val algorithm = Algorithm.HMAC256(jwtTokenUtils.JWT_SECRET)
            val verifier = JWT
                    .require(algorithm)
                    .build()

            return verifier.verify(token)
        } catch (e: Exception) {
            throw JwtTokenInvalidException()
        }
    }
}