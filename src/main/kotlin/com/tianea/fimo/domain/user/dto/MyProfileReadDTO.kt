package com.tianea.fimo.domain.user.dto

import com.tianea.fimo.domain.user.entity.User

class MyProfileReadDTO (
    val id: String,
    val nickname: String,
    val archiveName: String,
    val profileImageUrl: String,
    val postCount: Int,
){
    companion object{
        fun from(user: User, postCount: Int): MyProfileReadDTO{
            return MyProfileReadDTO(
                id = user.id,
                nickname = user.nickname,
                archiveName = user.archiveName,
                profileImageUrl = user.profileImageUrl,
                postCount = postCount,
            )
        }
    }
}