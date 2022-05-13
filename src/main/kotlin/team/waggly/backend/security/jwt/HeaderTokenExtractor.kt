package team.waggly.backend.security.jwt

import javax.servlet.http.HttpServletRequest

class HeaderTokenExtractor {
    val HEADER_PREFIX = "Bearer "

    fun extract(header: String, request: HttpServletRequest):String {
        if (header.isNullOrBlank()){
            throw NoSuchElementException("올바른 JWT 정보가 아닙니다.")
        } else if (header.length < HEADER_PREFIX.length){
            throw NoSuchElementException("올바른 JWT 정보가 아닙니다.")
        }

        return header.substring(
                HEADER_PREFIX.length,
                header.length
        )
    }
}