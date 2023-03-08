package team.waggly.backend.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import team.waggly.backend.repository.UserRepository
import team.waggly.backend.security.filter.FormLoginFilter
import team.waggly.backend.security.filter.JwtAuthFilter
import team.waggly.backend.security.jwt.JwtTokenUtils
import team.waggly.backend.security.provider.FormLoginAuthProvider
import team.waggly.backend.security.provider.JWTAuthProvider

@Configuration
@EnableWebSecurity
class SecurityConfig(
        private val userDetailsServiceImpl: UserDetailsServiceImpl,
        private val userRepository: UserRepository,
        private val jwtTokenUtils: JwtTokenUtils,
) : WebSecurityConfigurerAdapter() {
    @Bean
    fun encodePassword() : BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
                .authenticationProvider(FormLoginAuthProvider(userDetailsServiceImpl, encodePassword()))
                .authenticationProvider(JWTAuthProvider(userRepository, jwtTokenUtils))
    }

    override fun configure(web: WebSecurity) {
        web
                .ignoring()
                .antMatchers("/h2-console/**")
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable().httpBasic()

        // cors설정 추가
        http
                .cors()
                .configurationSource(corsConfigurationSource())


        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
    }

    @Bean
    fun formLoginFilter() : FormLoginFilter {
        val formLoginFilter = FormLoginFilter(authenticationManager())
        formLoginFilter.setFilterProcessesUrl("/user/login")
        formLoginFilter.setAuthenticationSuccessHandler(FormLoginSuccessHandler(jwtTokenUtils))
        formLoginFilter.afterPropertiesSet()
        return formLoginFilter
    }

    @Bean
    fun jwtAuthFilter(): JwtAuthFilter {
        val skipPathList = mutableListOf<Pair<HttpMethod, String>>()

        // Common
        skipPathList.add(Pair(HttpMethod.GET, "/_c/**"))

        // DB
        skipPathList.add(Pair(HttpMethod.GET, "/h2-console/**"))
        skipPathList.add(Pair(HttpMethod.POST,"/h2-console/**"))

        // Sign Up
        skipPathList.add(Pair(HttpMethod.POST,"/user/signup"))
        skipPathList.add(Pair(HttpMethod.POST,"/user/email"))
        skipPathList.add(Pair(HttpMethod.GET,"/major"))
        skipPathList.add(Pair(HttpMethod.POST,"/major"))
        skipPathList.add(Pair(HttpMethod.POST,"/user/nickname"))
        skipPathList.add(Pair(HttpMethod.POST,"/user/email/certification"))

        // Find Password
        skipPathList.add(Pair(HttpMethod.POST,"/user/check/email"))

        // Home
        skipPathList.add(Pair(HttpMethod.GET,"/board/home"))

        skipPathList.add(Pair(HttpMethod.GET,"/home"))

        // Swagger
        skipPathList.add(Pair(HttpMethod.GET, "/docs/**"))

        // Chatting
        skipPathList.add(Pair(HttpMethod.GET, "/ws-stomp/**"))
        skipPathList.add(Pair(HttpMethod.POST, "/ws-stomp/**"))
        skipPathList.add(Pair(HttpMethod.GET, "/chat/**"))
        skipPathList.add(Pair(HttpMethod.GET, "/ws/**"))

        val matcher = FilterSkipMatcher(skipPathList, "/**")

        val filter = JwtAuthFilter(matcher)
        filter.setAuthenticationManager(super.authenticationManager())

        return filter
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.addAllowedOrigin("http://localhost:3000") // local 테스트 시
        configuration.allowCredentials = true
        configuration.addAllowedMethod("*")
        configuration.addAllowedHeader("*")
        configuration.addExposedHeader("Authorization")
        configuration.addAllowedOriginPattern("*") // 배포 전 모두 허용
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}