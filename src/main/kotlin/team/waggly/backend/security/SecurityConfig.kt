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

        val matcher = FilterSkipMatcher(skipPathList, "/**")

        val filter = JwtAuthFilter(matcher)
        filter.setAuthenticationManager(super.authenticationManager())

        return filter
    }
}