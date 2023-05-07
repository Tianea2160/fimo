package com.tianea.fimo.domain.follow.dto

import com.tianea.fimo.domain.user.dto.UserReadDTO
import com.tianea.fimo.domain.user.entity.User

class FollowReadDTO(
    val user: UserReadDTO,
    val postCount: Int,
    val status: FollowStatus
) {
    companion object {
        fun from(user: User, status: FollowStatus, postCount: Int): FollowReadDTO {
            return FollowReadDTO(
                user = UserReadDTO.from(user),
                postCount = postCount,
                status = status
            )
        }
    }

    override fun toString(): String = "FollowReadDTO(user=$user, postCount=$postCount, status=$status)"
}

enum class FollowStatus {
    FOLLOWING, FOLLOWED, MUTUAL
}