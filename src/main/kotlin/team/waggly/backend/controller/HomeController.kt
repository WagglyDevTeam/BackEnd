package team.waggly.backend.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.home.HomeResponseDto
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.HomeService

@RestController
class HomeController(
    private val homeService: HomeService
) {
    @GetMapping("/home")
    fun getHome(@RequestParam userId: Long? = null): ResponseDto<HomeResponseDto> {
        return ResponseDto(homeService.getHome(userId))
    }
}