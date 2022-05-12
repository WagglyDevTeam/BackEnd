package team.waggly.backend.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import team.waggly.backend.repository.UserRepository
import java.lang.IllegalArgumentException

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails? {
        val user = userRepository.findByEmail(username) ?: throw IllegalArgumentException() // 추후 익셉션 구현 필요... 인증, 인가
        return UserDetailsImpl(user)
    }
}