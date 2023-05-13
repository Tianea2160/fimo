package com.tianea.fimo.domain.post.service

import com.tianea.fimo.domain.post.dto.PostCreateDTO
import com.tianea.fimo.domain.post.dto.PostItemCreateDTO
import com.tianea.fimo.domain.post.entity.PostItem
import com.tianea.fimo.domain.post.repository.PostClickRepository
import com.tianea.fimo.domain.post.repository.PostItemRepository
import com.tianea.fimo.domain.post.repository.PostRepository
import com.tianea.fimo.domain.user.entity.User
import com.tianea.fimo.domain.user.repository.UserRepository
import com.tianea.fimo.shared.provider.IdentifierProvider
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull

class PostServiceTest : BehaviorSpec({
    val userRepository: UserRepository = mockk()
    val postRepository: PostRepository = mockk()
    val postItemRepository: PostItemRepository = mockk()
    val postClickRepository: PostClickRepository = mockk()
    val provider: IdentifierProvider = mockk()
    val postService = PostService(
        userRepository,
        postRepository,
        postItemRepository,
        postClickRepository,
        provider
    )

    given("생성하고 싶은 게시글이 있을 때") {
        val userA = "userA"
        val create = PostCreateDTO(
            items = listOf(
                PostItemCreateDTO(
                    imageUrl = "test image",
                    content = "test content"
                )
            )
        )

        `when`("게시글 생성 메서드를 호출하면") {
            every { userRepository.findByIdOrNull(userA) } returns User(
                id = userA,
                nickname = "user_a",
                archiveName = "user_archive_a",
            )

            every { provider.generateId() } returns "test id"
            every { postRepository.save(any()) } answers { firstArg() }
            every { postItemRepository.saveAll(any<List<PostItem>>()) } answers { firstArg() }
            every { postClickRepository.existsByPostIdAndUserId(any(), any()) } returns false

            val read = postService.createPost(userA, create)

            then("게시글이 생성되고 PostReadDTO가 반환되어야한다.") {
                read.id shouldBe "test id"
                read.user.id shouldBe userA
                read.user.nickname shouldBe "user_a"
                read.user.archiveName shouldBe "user_archive_a"
                read.items.size shouldBe 1
                read.items[0].imageUrl shouldBe "test image"
                read.items[0].content shouldBe "test content"
            }
        }
    }
})