package com.tianea.fimo.domain.post.dto

import com.tianea.fimo.domain.post.entity.Post
import com.tianea.fimo.domain.post.entity.PostItem
import com.tianea.fimo.domain.user.dto.UserReadDTO
import com.tianea.fimo.domain.user.entity.User
import java.time.LocalDateTime

class PostReadDTO(
    val id: String,
    val user: UserReadDTO,
    val favorite: Long,
    val isClicked: Boolean,
    val createdAt: LocalDateTime,
    val items: List<PostItemReadDTO>
) {
    companion object {
        fun from(user: User, post: Post, items: List<PostItem>, isClicked: Boolean) = PostReadDTO(
            id = post.id,
            favorite = post.favorite,
            isClicked = isClicked,
            user = UserReadDTO.from(user),
            createdAt = post.createdAt,
            items = items.map { item -> PostItemReadDTO(item.imageUrl, item.content) }
        )
    }
}

class PostItemReadDTO(
    val imageUrl: String,
    val content: String,
)