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
<<<<<<< HEAD
    private val userDetailsServiceImpl: UserDetailsServiceImpl,
    private val userRepository: UserRepository
=======
        private val userDetailsServiceImpl: UserDetailsServiceImpl,
        private val userRepository: UserRepository
>>>>>>> 17a95112e49562fb52eeb4f8841ed553a3880a4b
) : WebSecurityConfigurerAdapter() {
    private val headerTokenExtractor = HeaderTokenExtractor()

    @Bean
    fun encodePassword() : BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
<<<<<<< HEAD
            .authenticationProvider(FormLoginAuthProvider(userDetailsServiceImpl, encodePassword()))
            .authenticationProvider(JWTAuthProvider(userRepository))
=======
                .authenticationProvider(FormLoginAuthProvider(userDetailsServiceImpl, encodePassword()))
                .authenticationProvider(JWTAuthProvider(userRepository))
>>>>>>> 17a95112e49562fb52eeb4f8841ed553a3880a4b
    }

    override fun configure(web: WebSecurity) {
        web
<<<<<<< HEAD
            .ignoring()
            .antMatchers("/h2-console/**")
=======
                .ignoring()
                .antMatchers("/h2-console/**")
>>>>>>> 17a95112e49562fb52eeb4f8841ed553a3880a4b
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable().httpBasic()

        http.sessionManagement()
<<<<<<< HEAD
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http
            .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter::class.java)

        http.authorizeRequests()
            .antMatchers("/h2-console/**", "/user/signup").permitAll()
            .anyRequest().permitAll()
            .and()
            .exceptionHandling()
=======
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http
                .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter::class.java)

        http.authorizeRequests()
                .antMatchers("/h2-console/**", "/user/signup").permitAll()
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
>>>>>>> 17a95112e49562fb52eeb4f8841ed553a3880a4b
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
<<<<<<< HEAD
        skipPathList.add("POST,/h2-console/**")
        skipPathList.add("POST,/user/signup")

=======
        skipPathList.add("POST,/user/signup")
        skipPathList.add("POST,/user/email")
        skipPathList.add("POST,/user/email/certification")
>>>>>>> 17a95112e49562fb52eeb4f8841ed553a3880a4b
        val matcher = FilterSkipMatcher(skipPathList, "/**")

        val filter = JwtAuthFilter(headerTokenExtractor, matcher)
        filter.setAuthenticationManager(super.authenticationManager())

        return filter
    }
}