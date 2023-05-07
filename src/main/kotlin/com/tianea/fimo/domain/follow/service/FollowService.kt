package com.tianea.fimo.domain.follow.service

import com.tianea.fimo.domain.follow.dto.FollowReadDTO
import com.tianea.fimo.domain.follow.dto.FollowStatus
import com.tianea.fimo.domain.follow.entity.Follow
import com.tianea.fimo.domain.follow.repository.FollowRepository
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

    @Transactional(readOnly = true)
    fun myFollowers(loginId: String): List<FollowReadDTO> {
        val follower = followRepository.findAllByFollower(loginId).toSet()
        val followee = followRepository.findAllByFollowee(loginId).toSet()

        val onlyMe = (follower - followee).map { follow ->
            val user = userRepository.findByIdOrNull(follow.followee) ?: throw UserNotFoundException()
            val postCount = postRepository.countByUserId(user.id)
            FollowReadDTO.from(user = user, status = FollowStatus.FOLLOWING, postCount = postCount)
        }
        val onlyYou = (followee - follower).map { follow ->
            val user = userRepository.findByIdOrNull(follow.follower) ?: throw UserNotFoundException()
            val postCount = postRepository.countByUserId(user.id)
            FollowReadDTO.from(user = user, status = FollowStatus.FOLLOWED, postCount = postCount)
        }
        val both = follower.intersect(followee).map { follow ->
            val user = userRepository.findByIdOrNull(follow.followee) ?: throw UserNotFoundException()
            val postCount = postRepository.countByUserId(user.id)
            FollowReadDTO.from(user = user, status = FollowStatus.MUTUAL, postCount = postCount)
        }
        return onlyMe + onlyYou + both
    }

    @Transactional
    fun follow(loginId: String, followee: String) {
        if (followRepository.existsByFollowerAndFollowee(loginId, followee)) return
        if(!userRepository.existsById(followee)) return
        val follow = Follow(provider.generateId(), loginId, followee)
        followRepository.save(follow)
    }

    @Transactional
    fun unfollow(loginId: String, followee: String) {
        followRepository.deleteByFollowerAndFollowee(loginId, followee)
    }
}