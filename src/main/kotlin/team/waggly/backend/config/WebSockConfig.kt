package team.waggly.backend.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration




@Configuration
@EnableWebSocketMessageBroker
class WebSockConfig(
        private val stompHandler: StompHandler
): WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/sub")
        config.setApplicationDestinationPrefixes("/pub")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS()
    }

    override fun configureWebSocketTransport(registration: WebSocketTransportRegistration) {
        registration.setMessageSizeLimit(50 * 1024 * 1024) // 메세지 크기 제한 오류 방지(이 코드가 없으면 byte code를 보낼때 소켓 연결이 끊길 수 있음)
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
//        registration.interceptors(stompHandler)
    }
}