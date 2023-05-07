package com.tianea.fimo.domain.user.dto

import com.tianea.fimo.domain.follow.dto.FollowStatus
import com.tianea.fimo.domain.user.entity.User

class ProfileReadDTO(
    val id: String,
    val nickname: String,
    val archiveName: String,
    val profileImageUrl: String,
    val postCount: Int,
    val status: FollowStatus
) {
    companion object {
        fun from(user: User, postCount: Int, status: FollowStatus): ProfileReadDTO {
            return ProfileReadDTO(
                id = user.id,
                nickname = user.nickname,
                archiveName = user.archiveName,
                profileImageUrl = user.profileImageUrl,
                postCount = postCount,
                status = status
            )
        }
    }
}