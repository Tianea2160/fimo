package com.tianea.fimo.domain.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
class User(
    @Id
    @Column(name = "user_id")
    val id: String,
    @Column(name = "nickname", unique = true)
    val nickname: String = UUID.randomUUID().toString(),
    @Column(name = "archive_name", unique = true)
    val archiveName: String = UUID.randomUUID().toString(),
    @Column(name = "profile_image_url", unique = true)
    val profileImageUrl: String = UUID.randomUUID().toString(),
)