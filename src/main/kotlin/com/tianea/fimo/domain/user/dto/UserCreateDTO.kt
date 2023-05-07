package com.tianea.fimo.domain.user.dto

import com.tianea.fimo.domain.user.entity.User

class UserCreateDTO(
    val id: String,
    val nickname: String,
    val archiveName: String,
    val profileImageUrl: String
) {
    fun toEntity(): User = User(
        id = id,
        nickname = nickname,
        archiveName = archiveName,
        profileImageUrl = profileImageUrl
    )
}