package com.tianea.fimo.domain.report.service

import com.tianea.fimo.domain.report.dto.ReportCreateDTO
import com.tianea.fimo.domain.report.repository.ReportRepository
import com.tianea.fimo.shared.provider.IdentifierProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReportService(
    private val reportRepository: ReportRepository,
    private val provider: IdentifierProvider
){
    @Transactional
    fun createReport(loginId : String, create : ReportCreateDTO){
        val report = create.toEntity(provider.generateId(),loginId )
        reportRepository.save(report)
    }


    @Transactional
    fun deleteAll(loginId:String){
        reportRepository.deleteAllByUserId(loginId)
    }
}