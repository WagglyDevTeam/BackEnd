package team.waggly.backend.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtDecoder() {
    private val jwtTokenUtils = JwtTokenUtils()
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun decodeUsername(token: String): String {
        val decodedJWT = isValidToken(token) ?: throw IllegalArgumentException("유효한 토큰이 아닙니다")

        val expiredDate = decodedJWT
                .getClaim(jwtTokenUtils.CLAIM_EXPIRED_DATE)
                .asDate()

        val now = Date()
        if (expiredDate.before(now)) {
            throw java.lang.IllegalArgumentException("유효한 토큰이 아닙니다")
        }

        return decodedJWT
                .getClaim(jwtTokenUtils.CLAIM_USER_NAME)
                .asString()
    }

    fun isValidToken(token: String):DecodedJWT? {
        var jwt: DecodedJWT? = null
        try {
            val algorithm = Algorithm.HMAC256(jwtTokenUtils.JWT_SECRET)
            val verifier = JWT
                    .require(algorithm)
                    .build()

            jwt = verifier.verify(token)
        } catch (e: Exception) {
            log.error(e.message)
        }

        return jwt
    }
}