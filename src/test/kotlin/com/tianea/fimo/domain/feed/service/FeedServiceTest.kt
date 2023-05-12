package com.tianea.fimo.domain.feed.service

import com.tianea.fimo.domain.follow.entity.Follow
import com.tianea.fimo.domain.follow.repository.FollowRepository
import com.tianea.fimo.domain.post.entity.Post
import com.tianea.fimo.domain.post.repository.PostClickRepository
import com.tianea.fimo.domain.post.repository.PostItemRepository
import com.tianea.fimo.domain.post.repository.PostRepository
import com.tianea.fimo.domain.user.entity.User
import com.tianea.fimo.domain.user.repository.UserRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

class FeedServiceTest : BehaviorSpec({
    val followRepository: FollowRepository = mockk()
    val postRepository: PostRepository = mockk()
    val userRepository: UserRepository = mockk()
    val postClickRepository: PostClickRepository = mockk()
    val postItemRepository: PostItemRepository = mockk()
    val feedService = FeedService(
        followRepository,
        postRepository,
        userRepository,
        postClickRepository,
        postItemRepository
    )

    given("나와 내가 팔로잉한 2명의 사용자가 있고 각각 3개의 포스트를 가지고 있을 때") {
        val userA = "userA"
        val userB = "userB"
        val userC = "userC"

        `when`("내가 피드 조회를 하면") {
            val posts = mutableListOf<Post>()

            for (i in 0 until 3) {
                posts.add(
                    Post(
                        id = "post$i",
                        userId = userA,
                        createdAt = LocalDateTime.of(2021, 10, 11, 10, i),
                    )
                )
            }
            for (i in 3 until 6) {
                posts.add(
                    Post(
                        id = "post$i",
                        userId = userB,
                        createdAt = LocalDateTime.of(2021, 10, 11, 10, i),
                    )
                )
            }
            for (i in 6 until 9) {
                posts.add(
                    Post(
                        id = "post$i",
                        userId = userC,
                        createdAt = LocalDateTime.of(2021, 10, 11, 10, i),
                    )
                )
            }

            every { postRepository.findAllByUserIdInOrderByCreatedAtDesc(any()) } returns posts
            every { userRepository.findByIdOrNull(userA) } returns User(
                id = userA,
                nickname = userA,
                archiveName = "userA archive",
                profileImageUrl = "userA profile image url",
            )
            every { userRepository.findByIdOrNull(userB) } returns User(
                id = userB,
                nickname = userB,
                archiveName = "userB archive",
                profileImageUrl = "userB profile image url",
            )
            every { userRepository.findByIdOrNull(userC) } returns User(
                id = userC,
                nickname = userC,
                archiveName = "userC archive",
                profileImageUrl = "userC profile image url",
            )
            every { postClickRepository.existsByPostIdAndUserId(any(), any()) } returns false
            every { postItemRepository.findAllByPostId(any()) } returns emptyList()
            every { followRepository.findAllByFollower(userA) } returns listOf(
                Follow(
                    id = "follow1",
                    follower = userA,
                    followee = userB
                ),
                Follow(
                    id = "follow2",
                    follower = userA,
                    followee = userC
                )
            )

            val reads = feedService.getFeed(userA)
            then("총 9개의 피드가 시간 순서대로 조회가 되어야한다.") {
                for (i in 0 until 9) {
                    reads[i].id shouldBe posts[i].id
                    reads[i].user.id shouldBe posts[i].userId
                    reads[i].createdAt shouldBe posts[i].createdAt
                    reads[i].isClicked shouldBe false
                    reads[i].items shouldBe emptyList()
                }
            }
        }
    }
})