package com.tianea.fimo.domain.post.repository

import com.tianea.fimo.domain.post.entity.Post
import com.tianea.fimo.domain.post.entity.PostClick
import com.tianea.fimo.domain.post.entity.PostItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository  : JpaRepository<Post, String>


@Repository
interface PostItemRepository  : JpaRepository<PostItem, String>

@Repository
interface PostClickRepository  : JpaRepository<PostClick, String>{
    fun existsByPostIdAndUserId(postId :String, userId :String) : Boolean
}