package com.tianea.fimo.domain.user.service

import com.tianea.fimo.domain.follow.service.FollowService
import com.tianea.fimo.domain.post.service.PostService
import com.tianea.fimo.domain.report.service.ReportService
import com.tianea.fimo.domain.user.dto.MyProfileReadDTO
import com.tianea.fimo.domain.user.dto.ProfileReadDTO
import com.tianea.fimo.domain.user.dto.ProfileUpdateDTO
import com.tianea.fimo.domain.user.dto.UserCreateDTO
import com.tianea.fimo.domain.user.error.UserNotFoundException
import com.tianea.fimo.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val postService: PostService,
    private val followService: FollowService,
    private val reportService: ReportService,
) {
    @Transactional
    fun createUser(create:UserCreateDTO){
        val user = create.toEntity()
        userRepository.save(user)
    }

    @Transactional(readOnly = true)
    fun isValidNickname(nickname: String) = !userRepository.existsByNickname(nickname)

    @Transactional(readOnly = true)
    fun isValidArchiveName(archiveName: String) = !userRepository.existsByArchiveName(archiveName)

    @Transactional(readOnly = true)
    fun isValidUserId(userId: String) = !userRepository.existsById(userId)

    @Transactional
    fun updateProfile(loginId: String, update: ProfileUpdateDTO): MyProfileReadDTO {
        val user = userRepository.findByIdOrNull(loginId) ?: throw UserNotFoundException()
        user.updateProfile(update.nickname, update.archiveName, update.profileImageUrl)
        val count = postService.countPosts(user.id)
        return MyProfileReadDTO.from(user = user, postCount = count)
    }

    @Transactional(readOnly = true)
    fun getMyProfile(loginId: String): MyProfileReadDTO {
        val user = userRepository.findByIdOrNull(loginId) ?: throw UserNotFoundException()
        val count = postService.countPosts(user.id)
        return MyProfileReadDTO.from(user = user, postCount = count)
    }

    @Transactional(readOnly = true)
    fun getUserProfile(loginId: String, profileUserId: String): ProfileReadDTO {
        val user = userRepository.findByIdOrNull(profileUserId) ?: throw UserNotFoundException()
        val status = followService.getFollowStatus(loginId, profileUserId)
        val count = postService.countPosts(user.id)
        return ProfileReadDTO.from(user = user, postCount = count, status = status)
    }

    @Transactional
    fun withdrawal(loginId: String) {
        // 사용자 정보 삭제
        userRepository.deleteById(loginId)
        // 사용자 게시글 삭제
        postService.deleteAll(loginId)
        // 사용자 팔로우 팔로잉 삭제
        followService.deleteAll(loginId)
        // 사용자 신고 삭제
        reportService.deleteAll(loginId)
    }
}