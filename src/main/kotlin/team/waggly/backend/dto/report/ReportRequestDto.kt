package team.waggly.backend.dto.report

import com.fasterxml.jackson.annotation.JsonIgnore
import team.waggly.backend.model.Report

class ReportRequestDto(
        val id: Long,
        val reportedUserId: Long,
        val reportType: String,

        @JsonIgnore
        var reportUserId: Long? = null,
) {
    fun generateReport(): Report {
        return Report(
                reportType = reportType,
                reportUserId = reportUserId!!,
                reportedUserId = reportedUserId,
                reason = reportType,
        )
    }
}