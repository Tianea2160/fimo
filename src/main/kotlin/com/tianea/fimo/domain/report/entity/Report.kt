package com.tianea.fimo.domain.report.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "reports")
class Report(
    @Id
    @Column(name = "report_id")
    val id: String,
    @Column(name = "post_id")
    val postId: String,// 신고 당한 게시글
    @Column(name = "user_id")
    val userId: String,// 신고한 사람
)