package com.tianea.fimo.domain.report.dto

import com.tianea.fimo.domain.report.entity.Report


class ReportCreateDTO (
    val postId : String,
){
    fun toEntity(id : String, userId : String) : Report = Report(id, postId, userId)
}