package team.waggly.backend.security.jwt

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import team.waggly.backend.exception.security.JwtTokenInvalidException
import team.waggly.backend.security.UserDetailsImpl
import java.util.*

@Component
class JwtTokenUtils {
//    @Value("\${spring.auth.secret.key}")
    var JWT_SECRET: String = "asdf"

    private val SEC = 1
    private val MINUTE = 60 * SEC
    private val HOUR = 60 * MINUTE
    private val DAY = 24 * HOUR

    private val JWT_TOKEN_VALID_MILLI_SEC = 3 * DAY * 1000

    internal val CLAIM_EXPIRED_DATE = "EXPIRED_DATE"
    internal val CLAIM_USER_NAME = "USER_NAME"

    fun generateJwtToken(userDetailsImpl: UserDetailsImpl): String {
        try {
            return JWT.create()
                    .withIssuer("waggly")
                    .withClaim(CLAIM_USER_NAME, userDetailsImpl.username)
                    .withClaim(CLAIM_EXPIRED_DATE, Date(System.currentTimeMillis() + JWT_TOKEN_VALID_MILLI_SEC))
                    .sign(Algorithm.HMAC256(JWT_SECRET)) as String
        } catch (e: Exception) {
            println(e)
            throw JwtTokenInvalidException("Error Create JWT Token")
        }
    }
}