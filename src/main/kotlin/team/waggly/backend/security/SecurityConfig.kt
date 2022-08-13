package team.waggly.backend.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import team.waggly.backend.commomenum.HttpMethodEnum
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
        val skipPathList = mutableListOf<Pair<HttpMethodEnum, String>>()

        skipPathList.add(Pair(HttpMethodEnum.GET, "/h2-console/**"))
        skipPathList.add(Pair(HttpMethodEnum.POST,"/h2-console/**"))
        skipPathList.add(Pair(HttpMethodEnum.POST,"/user/signup"))
        skipPathList.add(Pair(HttpMethodEnum.POST,"/user/email"))
        skipPathList.add(Pair(HttpMethodEnum.POST,"/major"))
        skipPathList.add(Pair(HttpMethodEnum.POST,"/user/nickname"))
        skipPathList.add(Pair(HttpMethodEnum.POST,"/user/email/certification"))
        skipPathList.add(Pair(HttpMethodEnum.GET,"/board/home"))

        val matcher = FilterSkipMatcher(skipPathList, "/**")

        val filter = JwtAuthFilter(matcher)
        filter.setAuthenticationManager(super.authenticationManager())

        return filter
    }
}