package com.tianea.fimo.domain.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
    @Id
    @Column(name = "user_id")
    val id: String,
    @Column(name = "nickname")
    val nickname: String = "default",
    @Column(name = "archive_name")
    val archiveName: String = "default",
    @Column(name = "profile_image_url")
    val profileImageUrl: String = "default",
)