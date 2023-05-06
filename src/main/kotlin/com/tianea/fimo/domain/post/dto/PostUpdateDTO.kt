package com.tianea.fimo.domain.post.dto

class PostUpdateDTO(
    val items: PostItemUpdateDTO
)

class PostItemUpdateDTO(
    val imageUrl: String,
    val content: String
)