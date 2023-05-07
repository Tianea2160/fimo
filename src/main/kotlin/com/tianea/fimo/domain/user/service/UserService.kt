package com.tianea.fimo.domain.user.service

import com.tianea.fimo.domain.follow.service.FollowService
import com.tianea.fimo.domain.post.repository.PostRepository
import com.tianea.fimo.domain.user.dto.MyProfileReadDTO
import com.tianea.fimo.domain.user.dto.ProfileReadDTO
import com.tianea.fimo.domain.user.dto.ProfileUpdateDTO
import com.tianea.fimo.domain.user.dto.UserReadDTO
import com.tianea.fimo.domain.user.error.UserNotFoundException
import com.tianea.fimo.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val followService: FollowService
) {
    @Transactional(readOnly = true)
    fun validateNickname(nickname: String) = !userRepository.existsByNickname(nickname)

    @Transactional(readOnly = true)
    fun validateArchiveName(archiveName: String) = !userRepository.existsByArchiveName(archiveName)

    @Transactional
    fun updateProfile(loginId: String, update: ProfileUpdateDTO): MyProfileReadDTO {
        val user = userRepository.findByIdOrNull(loginId) ?: throw UserNotFoundException()
        user.updateProfile(update.nickname, update.archiveName, update.profileImageUrl)
        val postCount = postRepository.countByUserId(user.id)
        return MyProfileReadDTO.from(user = user, postCount = postCount)
    }

    fun getMyProfile(loginId: String): MyProfileReadDTO {
        val user = userRepository.findByIdOrNull(loginId) ?: throw UserNotFoundException()
        val postCount = postRepository.countByUserId(user.id)
        return MyProfileReadDTO.from(user = user, postCount = postCount)
    }

    fun getUserProfile(loginId: String, profileUserId: String): ProfileReadDTO {
        val user = userRepository.findByIdOrNull(profileUserId) ?: throw UserNotFoundException()
        val count = postRepository.countByUserId(user.id)
        val status = followService.getFollowStatus(loginId, profileUserId)
        return ProfileReadDTO.from(user = user, postCount = count, status = status)
    }
}