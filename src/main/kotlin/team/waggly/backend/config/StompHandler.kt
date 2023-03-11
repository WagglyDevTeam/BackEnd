package team.waggly.backend.config

import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component
import team.waggly.backend.security.jwt.JwtDecoder
import javax.transaction.Transactional

@Component
@Transactional
class StompHandler(
        private val jwtDecoder: JwtDecoder
): ChannelInterceptor {
}