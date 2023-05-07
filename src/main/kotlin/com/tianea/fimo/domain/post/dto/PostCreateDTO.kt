package com.tianea.fimo.domain.post.dto

import com.tianea.fimo.domain.post.entity.Post
import com.tianea.fimo.domain.post.entity.PostItem
import com.tianea.fimo.shared.provider.IdentifierProvider


class PostCreateDTO(
    val items: List<PostItemCreateDTO>
) {
    fun toEntity(id: String, userId: String): Post {
        return Post(id = id, userId = userId)
    }

    fun createItems(postId: String, provider: IdentifierProvider): List<PostItem> =
        items.map { item -> item.toEntity(provider.generateId(), postId) }
}

class PostItemCreateDTO(
    val imageUrl: String,
    val content: String
) {
    fun toEntity(id: String, postId: String): PostItem =
        PostItem(id = id, imageUrl = imageUrl, content = content, postId = postId)
}