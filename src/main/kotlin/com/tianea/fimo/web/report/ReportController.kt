package com.tianea.fimo.web.report

import com.tianea.fimo.domain.report.dto.ReportCreateDTO
import com.tianea.fimo.domain.report.service.ReportService
import com.tianea.fimo.shared.dto.CommonResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/report")
class ReportController(
    private val reportService: ReportService
) {

    @PostMapping("/create")
    fun createReport(principal: Principal, @RequestBody create: ReportCreateDTO): CommonResponse {
        reportService.createReport(loginId = principal.name, create = create)
        return CommonResponse("create report success", "create.report.success", 200)
    }
}