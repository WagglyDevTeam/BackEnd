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
import team.waggly.backend.security.filter.FormLoginFilter
import team.waggly.backend.security.filter.JwtAuthFilter
import team.waggly.backend.security.provider.FormLoginAuthProvider

@Configuration
@EnableWebSecurity
class SecurityConfig(private val userDetailsServiceImpl: UserDetailsServiceImpl) : WebSecurityConfigurerAdapter() {
    @Bean
    fun encodePassword() : BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(FormLoginAuthProvider(userDetailsServiceImpl, encodePassword()))
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

        http.authorizeRequests()
                .antMatchers("/h2-console/**", "/user/signup").permitAll()
                .anyRequest().authenticated()
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

        val matcher = FilterSkipMatcher(skipPathList, "/**")

        val filter = JwtAuthFilter(headerTokenExtractor, matcher)
        filter.setAuthenticationManager(super.authenticationManager())

        return filter
    }
}