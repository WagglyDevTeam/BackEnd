package team.waggly.backend.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import team.waggly.backend.model.User

class UserDetailsImpl(val user: User) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = ArrayList<GrantedAuthority>()
        authorities.add(SimpleGrantedAuthority(user.auth.toString()))
        return authorities
    }

    override fun getPassword() = user.password
    override fun getUsername() = user.email
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
}