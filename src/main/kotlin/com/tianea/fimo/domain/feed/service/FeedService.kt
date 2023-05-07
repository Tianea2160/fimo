package com.tianea.fimo.domain.feed.service

import com.tianea.fimo.domain.follow.repository.FollowRepository
import com.tianea.fimo.domain.post.dto.PostReadDTO
import com.tianea.fimo.domain.post.repository.PostClickRepository
import com.tianea.fimo.domain.post.repository.PostItemRepository
import com.tianea.fimo.domain.post.repository.PostRepository
import com.tianea.fimo.domain.user.error.UserNotFoundException
import com.tianea.fimo.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeedService(
    private val followRepository: FollowRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val postClickRepository: PostClickRepository,
    private val postItemRepository: PostItemRepository,
) {
    @Transactional(readOnly = true)
    fun getFeed(loginId: String): List<PostReadDTO> {
        val follows = followRepository.findAllByFollower(loginId)
        val ids = follows.map { follow -> follow.followee } + listOf(loginId)
        return postRepository.findAllByUserIdInOrderByCreatedAtDesc(ids)
            .map { post ->
                val user = userRepository.findByIdOrNull(post.userId) ?: throw UserNotFoundException()
                val isClicked = postClickRepository.existsByPostIdAndUserId(post.id, loginId)
                val items = postItemRepository.findAllByPostId(post.id)
                PostReadDTO.from(user = user, post = post, items = items, isClicked = isClicked)
            }
    }
}