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
import team.waggly.backend.repository.UserRepository
import team.waggly.backend.security.filter.FormLoginFilter
import team.waggly.backend.security.filter.JwtAuthFilter
import team.waggly.backend.security.jwt.HeaderTokenExtractor
import team.waggly.backend.security.provider.FormLoginAuthProvider
import team.waggly.backend.security.provider.JWTAuthProvider

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userDetailsServiceImpl: UserDetailsServiceImpl,
    private val userRepository: UserRepository
) : WebSecurityConfigurerAdapter() {
    private val headerTokenExtractor = HeaderTokenExtractor()

    @Bean
    fun encodePassword() : BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .authenticationProvider(FormLoginAuthProvider(userDetailsServiceImpl, encodePassword()))
            .authenticationProvider(JWTAuthProvider(userRepository))
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

        http
            .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter::class.java)

        http.authorizeRequests()
            .antMatchers("/h2-console/**", "/user/signup").permitAll()
            .anyRequest().permitAll()
            .and()
            .exceptionHandling()
    }

    @Bean
    fun formLoginFilter() : FormLoginFilter {
        val formLoginFilter = FormLoginFilter(authenticationManager())
        formLoginFilter.setFilterProcessesUrl("/user/login")
        formLoginFilter.setAuthenticationSuccessHandler(FormLoginSuccessHandler())
        formLoginFilter.afterPropertiesSet()
        return formLoginFilter
    }

    @Bean
    fun jwtAuthFilter(): JwtAuthFilter {
        val skipPathList = mutableListOf<String>()

        skipPathList.add("GET,/h2-console/**")
        skipPathList.add("POST,/user/signup")
        skipPathList.add("POST,/user/signup")
        skipPathList.add("POST,/user/email")
        skipPathList.add("POST,/major")
        skipPathList.add("POST,/user/nickname")
        skipPathList.add("POST,/user/email/certification")

        val matcher = FilterSkipMatcher(skipPathList, "/**")

        val filter = JwtAuthFilter(headerTokenExtractor, matcher)
        filter.setAuthenticationManager(super.authenticationManager())

        return filter
    }
}