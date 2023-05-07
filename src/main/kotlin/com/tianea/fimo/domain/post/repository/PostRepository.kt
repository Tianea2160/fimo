package com.tianea.fimo.domain.post.repository

import com.tianea.fimo.domain.post.entity.Post
import com.tianea.fimo.domain.post.entity.PostClick
import com.tianea.fimo.domain.post.entity.PostItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, String> {
    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    fun findAllSortedByCreatedAtDesc(): List<Post>
    fun countByUserId(userId: String): Int
    fun findAllByUserId(userId: String): List<Post>
    fun findAllByUserIdInOrderByCreatedAtDesc(ids: List<String>): List<Post>
    fun deleteAllByUserId(userId :String)
}


@Repository
interface PostItemRepository : JpaRepository<PostItem, String> {
    fun deleteAllByPostId(postId: String)
    fun findAllByPostId(postId: String): List<PostItem>
}

@Repository
interface PostClickRepository : JpaRepository<PostClick, String> {
    fun existsByPostIdAndUserId(postId: String, userId: String): Boolean
    fun deleteAllByUserId(userId:String)
    fun deleteAllByPostId(postId:String)
}