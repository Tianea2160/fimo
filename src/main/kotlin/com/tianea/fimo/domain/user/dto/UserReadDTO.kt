package com.tianea.fimo.domain.user.dto

import com.tianea.fimo.domain.user.entity.User


class UserReadDTO(
    val id: String,
    val nickname: String,
    val archiveName: String,
    val profileImageUrl : String,
) {
    companion object {
        fun from(user: User) = UserReadDTO(
            id = user.id,
            nickname = user.nickname,
            archiveName = user.archiveName,
            profileImageUrl = user.profileImageUrl
        )
    }

    override fun toString(): String = "UserReadDTO(id='$id', nickname='$nickname', archiveName='$archiveName')"
}