package com.tianea.fimo.domain.follow.service

import com.tianea.fimo.domain.follow.dto.FollowStatus
import com.tianea.fimo.domain.follow.entity.Follow
import com.tianea.fimo.domain.follow.repository.FollowRepository
import com.tianea.fimo.domain.post.repository.PostRepository
import com.tianea.fimo.domain.user.entity.User
import com.tianea.fimo.domain.user.repository.UserRepository
import com.tianea.fimo.shared.provider.IdentifierProvider
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class FollowServiceTest : BehaviorSpec({
    val followRepository: FollowRepository = mockk()
    val userRepository: UserRepository = mockk()
    val postRepository: PostRepository = mockk()
    val provider: IdentifierProvider = mockk()
    val followService = FollowService(followRepository, userRepository, postRepository, provider)

    given("사용자가 3명 있을 때 ") {
        val userA = "userA"
        val userB = "userB"
        val userC = "userC"
        val userD = "userD"

        every { userRepository.findByIdOrNull(userA) } returns User(
            id = userA,
            nickname = "user_a",
            archiveName = "user_archive_a",
        )
        every { userRepository.findByIdOrNull(userB) } returns User(
            id = userB,
            nickname = "user_b",
            archiveName = "user_archive_b",
        )
        every { userRepository.findByIdOrNull(userC) } returns User(
            id = userC,
            nickname = "user_c",
            archiveName = "user_archive_c",
        )
        every { userRepository.findByIdOrNull(userD) } returns User(
            id = userD,
            nickname = "user_d",
            archiveName = "user_archive_d",
        )
        every { postRepository.countByUserId(any()) } returns 0

        `when`("어느 한 사용자가 팔로우 목록을 조회하면") {
            every { followRepository.findAllByFollower(userA) } returns listOf(
                Follow("1", userA, userB),
                Follow("2", userA, userC),
            )

            every { followRepository.findAllByFollowee(userA) } returns listOf(
                Follow("3", userB, userA),
                Follow("4", userD, userA)
            )

            val reads = followService.myFollowers(userA)

            then("나만 팔로우, 너만 팔로우, 모두 팔로우가 FollowReadDTO로 반환된다.") {
                reads.size shouldBe 3
                reads.count { it.status == FollowStatus.FOLLOWING } shouldBe 1
                reads.count { it.status == FollowStatus.FOLLOWED } shouldBe 1
                reads.count { it.status == FollowStatus.MUTUAL } shouldBe 1
            }
        }
    }

    given("사용자 A, B가 있을 때") {
        val userA = "userA"
        val userB = "userB"

        `when`("A, B가 아무 관계가 아닐때, A가 B를 팔로우하면") {
            every { followRepository.existsByFollowerAndFollowee(userA, userB) } returns false
            every { userRepository.existsById(userB) } returns true
            every { followRepository.save(any()) } returns Follow("user_a id", userA, userB)
            every { provider.generateId() } returns "user_a id"

            followService.follow(userA, userB)
            then("팔로우가 생성되어야 한다.") {
                verify(exactly = 1) { followRepository.save(any()) }
            }
        }

        `when`("A가 B를 팔로우하고 있는 상태에서 A가 B를 언팔로우하면") {
            every { followRepository.deleteByFollowerAndFollowee(userA, userB) } returns Unit
            followService.unfollow(userA, userB)
            then("팔로우가 삭제되어야 한다.") {
                verify(exactly = 1) { followRepository.deleteByFollowerAndFollowee(userA, userB) }
            }
        }
    }
})