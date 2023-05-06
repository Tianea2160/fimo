package com.tianea.fimo.web.post

import com.tianea.fimo.domain.post.dto.PostCreateDTO
import com.tianea.fimo.domain.post.dto.PostReadDTO
import com.tianea.fimo.domain.post.service.PostService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal


@RestController
@RequestMapping("/api/v1/post")
class PostController(
    private val postService: PostService

){

    @PostMapping("/create")
    fun createPost(
        principal: Principal,
        @RequestBody create: PostCreateDTO
    ): PostReadDTO {
        return postService.createPost(principal.name, create)
    }

}