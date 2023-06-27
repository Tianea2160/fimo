package com.tianea.fimo.domain.follow.dto

import com.tianea.fimo.domain.follow.entity.Follow
import com.tianea.fimo.domain.user.entity.User
import java.time.LocalDateTime

class FollowReadDTO(
    val id: String,
    val nickname: String,
    val archiveName: String,
    val profileImageUrl: String,
    val postCount: Int,
    val status: FollowStatus,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(user: User, follow: Follow, postCount: Int, status: FollowStatus): FollowReadDTO {
            return FollowReadDTO(
                id = user.id,
                nickname = user.nickname,
                archiveName = user.archiveName,
                profileImageUrl = user.profileImageUrl,
                postCount = postCount,
                status = status,
                createdAt = follow.createdAt
            )
        }
    }
}

enum class FollowStatus {
    FOLLOWING, FOLLOWED, MUTUAL, NONE
}