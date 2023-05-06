package com.tianea.fimo.domain.post.entity

import com.tianea.fimo.domain.user.entity.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name = "posts")
class Post(
    @Id
    @Column(name = "post_id")
    val id: String,
    var favorite: Long = 0,
    @Column(name = "user_id")
    var userId: String,
    @Column(name = "created_at")
    val createdAt : LocalDateTime = LocalDateTime.now()
) {
    fun favoriteUp() = favorite++
}

@Entity
@Table(name = "post_items")
class PostItem(
    @Id
    @Column(name = "post_item_id")
    val id: String,
    @Column(name = "image_url")
    val imageUrl: String,
    @Column(name = "content")
    val content: String,
    @Column(name = "post_id")
    val postId: String,
)


@Entity
@Table(name = "post_click")
class PostClick(
    @Id
    @Column(name = "post_click_id")
    val id: String,
    @Column(name = "user_id")
    val userId: String,
    @Column(name = "post_id")
    val postId: String
)