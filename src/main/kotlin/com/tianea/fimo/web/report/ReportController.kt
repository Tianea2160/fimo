package com.tianea.fimo.web.report

import com.tianea.fimo.domain.report.dto.ReportCreateDTO
import com.tianea.fimo.domain.report.service.ReportService
import com.tianea.fimo.shared.dto.CommonResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Tag(name = "Report", description = "신고하기 기능이 있습니다.")
@RestController
@RequestMapping("/api/v1/report")
class ReportController(
    private val reportService: ReportService
) {

    @Operation(summary = "신고하기", description = "신고하기 기능입니다.")
    @PostMapping("/create")
    fun createReport(principal: Principal, @RequestBody create: ReportCreateDTO): CommonResponse {
        reportService.createReport(loginId = principal.name, create = create)
        return CommonResponse("create report success", "create.report.success", 200)
    }
}