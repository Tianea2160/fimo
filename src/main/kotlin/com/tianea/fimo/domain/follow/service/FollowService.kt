package com.tianea.fimo.domain.follow.service

import com.tianea.fimo.domain.follow.dto.FollowReadDTO
import com.tianea.fimo.domain.follow.dto.FollowStatus
import com.tianea.fimo.domain.follow.entity.Follow
import com.tianea.fimo.domain.follow.repository.FollowRepository
import com.tianea.fimo.domain.follow.service.FollowSortType.ALPAHABETICAL
import com.tianea.fimo.domain.follow.service.FollowSortType.CREATED
import com.tianea.fimo.domain.post.repository.PostRepository
import com.tianea.fimo.domain.user.error.UserNotFoundException
import com.tianea.fimo.domain.user.repository.UserRepository
import com.tianea.fimo.shared.provider.IdentifierProvider
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FollowService(
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val provider: IdentifierProvider
) {

    fun sortedFollowReadDTOList(follow : List<FollowReadDTO>, sortType: FollowSortType) =
        when (sortType) {
            CREATED -> follow.sortedByDescending { it.createdAt }
            ALPAHABETICAL -> follow.sortedBy { it.nickname }
        }


    @Transactional(readOnly = true)
    fun myFollowers(loginId: String, sortType: FollowSortType = CREATED): List<FollowReadDTO> {
        val follower = followRepository.findAllByFollower(loginId).toSet()
        val followee = followRepository.findAllByFollowee(loginId).toSet()

        val onlyMe = (follower - followee).map { follow ->
            val user = userRepository.findByIdOrNull(follow.followee) ?: throw UserNotFoundException()
            val postCount = postRepository.countByUserId(user.id)
            FollowReadDTO.from(user = user, follow, status = FollowStatus.FOLLOWING, postCount = postCount)
        }
        val onlyYou = (followee - follower).map { follow ->
            val user = userRepository.findByIdOrNull(follow.follower) ?: throw UserNotFoundException()
            val postCount = postRepository.countByUserId(user.id)
            FollowReadDTO.from(user = user, follow = follow, status = FollowStatus.FOLLOWED, postCount = postCount)
        }
        val both = follower.intersect(followee).map { follow ->
            val user = userRepository.findByIdOrNull(follow.followee) ?: throw UserNotFoundException()
            val postCount = postRepository.countByUserId(user.id)
            FollowReadDTO.from(user = user, follow = follow, status = FollowStatus.MUTUAL, postCount = postCount)
        }

        val result = onlyMe + onlyYou + both
        return sortedFollowReadDTOList(result, sortType)
    }

    @Transactional(readOnly = true)
    fun findFollowers(loginId: String, userId: String, sortType: FollowSortType = CREATED): List<FollowReadDTO> {
        val follower = followRepository.findAllByFollower(userId).toSet()
        val followee = followRepository.findAllByFollowee(userId).toSet()
        val loginUser = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()


        val onlyMe = (follower - followee).map { follow ->
            val user = userRepository.findByIdOrNull(follow.followee) ?: throw UserNotFoundException()
            val postCount = postRepository.countByUserId(user.id)
            val status = getFollowStatus(loginId, follow.followee)
            FollowReadDTO.from(user = user, follow, status = status, postCount = postCount)
        }
        val onlyYou = (followee - follower).map { follow ->
            val user = userRepository.findByIdOrNull(follow.follower) ?: throw UserNotFoundException()
            val postCount = postRepository.countByUserId(user.id)
            val status = getFollowStatus(loginId, follow.followee)
            FollowReadDTO.from(user = user, follow = follow, status = status, postCount = postCount)
        }

        val both = follower.intersect(followee).map { follow ->
            val user = userRepository.findByIdOrNull(follow.followee) ?: throw UserNotFoundException()
            val postCount = postRepository.countByUserId(user.id)
            val id = if (follow.follower == userId) follow.followee else follow.follower
            val status = getFollowStatus(loginId, id)
            FollowReadDTO.from(user = user, follow = follow, status = status, postCount = postCount)
        }

        val result = onlyMe + onlyYou + both
        return sortedFollowReadDTOList(result, sortType)
    }

    @Transactional
    fun follow(loginId: String, followee: String) {
        if (loginId == followee) return
        if (followRepository.existsByFollowerAndFollowee(loginId, followee)) return
        if (!userRepository.existsById(followee)) return
        val follow = Follow(provider.generateId(), loginId, followee)
        followRepository.save(follow)
    }

    @Transactional
    fun unfollow(loginId: String, followee: String) {
        if (loginId == followee) return
        followRepository.deleteByFollowerAndFollowee(loginId, followee)
    }


    @Transactional(readOnly = true)
    fun getFollowStatus(loginId: String, targetId: String): FollowStatus {
        val onlyMe = followRepository.existsByFollowerAndFollowee(loginId, targetId)
        val onlyYou = followRepository.existsByFollowerAndFollowee(targetId, loginId)
        return if (onlyMe && onlyYou) FollowStatus.MUTUAL
        else if (!onlyMe && onlyYou) FollowStatus.FOLLOWED
        else if (onlyMe && !onlyYou) FollowStatus.FOLLOWING
        else FollowStatus.NONE
    }

    @Transactional
    fun deleteAll(userId: String) {
        followRepository.deleteAllByFollowerOrFollowee(userId, userId)
    }

}

enum class FollowSortType {
    CREATED, ALPAHABETICAL
}