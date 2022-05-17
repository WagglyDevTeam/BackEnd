package team.waggly.backend.security.jwt

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm
import team.waggly.backend.security.UserDetailsImpl
import java.util.*

class JwtTokenUtils {
    private val SEC = 1
    private val MINUTE = 60 * SEC
    private val HOUR = 60 * MINUTE
    private val DAY = 24 * HOUR

    private val JWT_TOKEN_VALID_SEC = 3 * DAY
    private val JWT_TOKEN_VALID_MILLI_SEC = JWT_TOKEN_VALID_SEC * 1000

<<<<<<< HEAD
    val CLAIM_EXPIRED_DATE = "EXPIRED_DATE"
    val CLAIM_USER_NAME = "USER_NAME"
    val JWT_SECRET = "wagglybackend_!@#$%"
=======
    internal val CLAIM_EXPIRED_DATE = "EXPIRED_DATE"
    internal val CLAIM_USER_NAME = "USER_NAME"
    internal val JWT_SECRET = "wagglybackend_!@#$%"
>>>>>>> 17a95112e49562fb52eeb4f8841ed553a3880a4b

    fun generateJwtToken(userDetailsImpl: UserDetailsImpl): String? {
        var token: String? = null;
        try {
            token = JWT.create()
                    .withIssuer("waggly")
                    .withClaim(CLAIM_USER_NAME, userDetailsImpl.username)
                    .withClaim(CLAIM_EXPIRED_DATE, Date(System.currentTimeMillis() + JWT_TOKEN_VALID_MILLI_SEC))
                    .sign(Algorithm.HMAC256(JWT_SECRET)) as String
        } catch (e: Exception){
            println(e.message)
        }

        return token
    }
}