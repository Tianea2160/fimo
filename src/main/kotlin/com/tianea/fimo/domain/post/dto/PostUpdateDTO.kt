package com.tianea.fimo.domain.post.dto

import com.tianea.fimo.domain.post.entity.PostItem
import com.tianea.fimo.shared.provider.IdentifierProvider

class PostUpdateDTO(
    val items: List<PostItemUpdateDTO>
) {
    fun createPostItems(postId: String, provider: IdentifierProvider): List<PostItem> =
        items.map { item ->
            PostItem(
                id = provider.generateId(),
                postId = postId,
                imageUrl = item.imageUrl,
                content = item.content
            )
        }
}

class PostItemUpdateDTO(
    val imageUrl: String,
    val content: String
)