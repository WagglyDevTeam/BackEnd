package team.waggly.backend.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.report.ReportRequestDto
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.service.ReportService

@RestController
@RequestMapping("/report")
class ReportController(
        private val reportService: ReportService,
) {
    @PostMapping("/post")
    fun createReportForPost(
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
            @RequestBody reportRequestDto: ReportRequestDto
    ): ResponseDto<Any> {
        reportRequestDto.reportUserId = userDetailsImpl.user.id
        return reportService.createReportForPost(reportRequestDto)
    }

    @PostMapping("/comment")
    fun createReportForComment(
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
            @RequestBody reportRequestDto: ReportRequestDto
    ): ResponseDto<Any> {
        reportRequestDto.reportUserId = userDetailsImpl.user.id
        return reportService.createReportForComment(reportRequestDto)
    }

    @PostMapping("/reply")
    fun createReportForReply(
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
            @RequestBody reportRequestDto: ReportRequestDto
    ): ResponseDto<Any> {
        reportRequestDto.reportUserId = userDetailsImpl.user.id
        return reportService.createReportForComment(reportRequestDto)
    }

    @PostMapping("/groupchat/user")
    fun createReportForGroupChatUser(
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
            @RequestBody reportRequestDto: ReportRequestDto
    ): ResponseDto<Any> {
        reportRequestDto.reportUserId = userDetailsImpl.user.id
        return reportService.createReportForGroupChatUser(reportRequestDto)
    }

    @PostMapping("/chat/user")
    fun createReportForChatUser(
            @AuthenticationPrincipal userDetailsImpl: UserDetailsImpl,
            @RequestBody reportRequestDto: ReportRequestDto
    ): ResponseDto<Any> {
        reportRequestDto.reportUserId = userDetailsImpl.user.id
        return reportService.createReportForChatUser(reportRequestDto)
    }
}