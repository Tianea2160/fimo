package com.tianea.fimo.domain.post.service

import com.tianea.fimo.domain.post.dto.PostCreateDTO
import com.tianea.fimo.domain.post.dto.PostItemCreateDTO
import com.tianea.fimo.domain.post.dto.PostItemUpdateDTO
import com.tianea.fimo.domain.post.dto.PostUpdateDTO
import com.tianea.fimo.domain.post.entity.Post
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

    given("수정하고 싶은 게시글이 있을 때") {
        val user = User(
            id = "userA",
            nickname = "user_a",
            archiveName = "user_archive_a",
        )
        val postId = "test post id"
        val update = PostUpdateDTO(
            items = listOf(
                PostItemUpdateDTO(
                    imageUrl = "test image",
                    content = "test content"
                )
            )
        )
        `when`("게시글 수정 메서드를 호출하면") {
            every { userRepository.findByIdOrNull(user.id) } returns user
            every { postRepository.findByIdOrNull(postId) } returns Post(
                id = postId,
                userId = user.id,
            )

            every { postItemRepository.deleteAllByPostId(postId) } returns Unit
            every { postItemRepository.saveAll(any<List<PostItem>>()) } answers { firstArg() }
            every { postClickRepository.existsByPostIdAndUserId(any(), any()) } returns false

            val read = postService.updatePost(user.id, postId, update)

            then("게시글이 수정되어야한다.") {
                read.id shouldBe postId
                read.user.id shouldBe user.id
                read.user.nickname shouldBe user.nickname
                read.user.archiveName shouldBe user.archiveName
                read.items.size shouldBe 1

                for (i in 0..update.items.lastIndex) {
                    read.items[i].imageUrl shouldBe update.items[i].imageUrl
                    read.items[i].content shouldBe update.items[i].content
                }
            }
        }
    }
})