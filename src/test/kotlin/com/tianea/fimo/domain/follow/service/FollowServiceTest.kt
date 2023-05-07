package com.tianea.fimo.domain.follow.service

import com.ninjasquad.springmockk.MockkBean
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
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
@ExtendWith(MockKExtension::class)
class FollowServiceTest(
    @InjectMockKs
    private val followService: FollowService,
    @MockkBean
    private val followRepository: FollowRepository,
    @MockkBean
    private val userRepository: UserRepository,
    @MockkBean
    private val postRepository: PostRepository,
    @MockkBean
    private val provider: IdentifierProvider
) : BehaviorSpec({

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
})