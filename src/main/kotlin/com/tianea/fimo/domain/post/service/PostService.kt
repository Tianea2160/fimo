package com.tianea.fimo.domain.post.service

import com.tianea.fimo.domain.post.dto.PostCreateDTO
import com.tianea.fimo.domain.post.dto.PostReadDTO
import com.tianea.fimo.domain.post.dto.PostUpdateDTO
import com.tianea.fimo.domain.post.error.PostAuthorizationException
import com.tianea.fimo.domain.post.error.PostNotFoundException
import com.tianea.fimo.domain.post.repository.PostClickRepository
import com.tianea.fimo.domain.post.repository.PostItemRepository
import com.tianea.fimo.domain.post.repository.PostRepository
import com.tianea.fimo.domain.user.error.UserNotFoundException
import com.tianea.fimo.domain.user.repository.UserRepository
import com.tianea.fimo.shared.provider.IdentifierProvider
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val postItemRepository: PostItemRepository,
    private val postClickRepository: PostClickRepository,
    private val provider: IdentifierProvider
) {
    @Transactional
    fun createPost(loginId: String, create: PostCreateDTO): PostReadDTO {
        val user = userRepository.findByIdOrNull(loginId) ?: throw UserNotFoundException()
        val post = create.toEntity(id = provider.generateId(), userId = loginId)
        val items = create.createItems(postId = post.id, provider = provider)
        postRepository.save(post)
        postItemRepository.saveAll(items)
        val isClicked = postClickRepository.existsByPostIdAndUserId(postId = post.id, userId = loginId)
        return PostReadDTO.from(user, post, items, isClicked)
    }

    @Transactional
    fun updatePost(loginId: String, postId: String, update: PostUpdateDTO): PostReadDTO {
        val user = userRepository.findByIdOrNull(loginId) ?: throw UserNotFoundException()
        val post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
        if (user.id != post.userId) throw PostAuthorizationException()
        postItemRepository.deleteAllByPostId(post.id)
        val item = update.createPostItems(postId = post.id, provider = provider)
        postItemRepository.saveAll(item)
        val isClicked = postClickRepository.existsByPostIdAndUserId(userId = loginId, postId = postId)
        return PostReadDTO.from(user, post, item, isClicked)
    }

    @Transactional
    fun deletePost(loginId: String, postId: String) {
        val user = userRepository.findByIdOrNull(loginId) ?: throw UserNotFoundException()
        val post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
        if (user.id != post.userId) throw PostAuthorizationException()
        postItemRepository.deleteAllByPostId(post.id)
        postRepository.deleteById(post.id)
    }

    @Transactional(readOnly = true)
    fun findAll(loginId: String): List<PostReadDTO> {
        return postRepository.findAllSortedByCreatedAtDesc().map { post ->
            val items = postItemRepository.findAllByPostId(post.id)
            val user = userRepository.findByIdOrNull(post.userId) ?: throw UserNotFoundException()
            val isClicked = postClickRepository.existsByPostIdAndUserId(userId = loginId, postId = post.id)
            PostReadDTO.from(user, post, items, isClicked)
        }
    }

    @Transactional(readOnly = true)
    fun findById(loginId: String, postId: String): PostReadDTO {
        val post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
        val user = userRepository.findByIdOrNull(post.userId) ?: throw UserNotFoundException()
        val items = postItemRepository.findAllByPostId(post.id)
        val isClicked = postClickRepository.existsByPostIdAndUserId(userId = loginId, postId = post.id)
        return PostReadDTO.from(user, post, items, isClicked)
    }


    @Transactional(readOnly = true)
    fun findAllByUserId(loginId: String, userId: String): List<PostReadDTO> =
        postRepository.findAllByUserId(userId).map { post ->
            val items = postItemRepository.findAllByPostId(post.id)
            val user = userRepository.findByIdOrNull(post.userId) ?: throw UserNotFoundException()
            val isClicked = postClickRepository.existsByPostIdAndUserId(userId = loginId, postId = post.id)
            PostReadDTO.from(user, post, items, isClicked)
        }
}

