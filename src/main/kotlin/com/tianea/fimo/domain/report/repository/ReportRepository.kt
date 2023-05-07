package com.tianea.fimo.domain.report.repository

import com.tianea.fimo.domain.report.entity.Report
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository  : JpaRepository<Report, String>{
    fun deleteAllByUserId(userId:String)
}