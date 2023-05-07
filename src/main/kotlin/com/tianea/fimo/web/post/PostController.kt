package com.tianea.fimo.web.post

import com.tianea.fimo.domain.post.dto.PostCreateDTO
import com.tianea.fimo.domain.post.dto.PostReadDTO
import com.tianea.fimo.domain.post.dto.PostUpdateDTO
import com.tianea.fimo.domain.post.service.PostService
import com.tianea.fimo.shared.dto.CommonResponse
import org.springframework.web.bind.annotation.*
import java.security.Principal


@RestController
@RequestMapping("/api/v1/post")
class PostController(
    private val postService: PostService

) {
    @PostMapping("/create")
    fun createPost(
        principal: Principal,
        @RequestBody create: PostCreateDTO
    ): PostReadDTO = postService.createPost(principal.name, create)

    @PutMapping("/update/{postId}")
    fun updatePost(
        @PathVariable("postId")
        postId: String,
        principal: Principal,
        @RequestBody update: PostUpdateDTO
    ): PostReadDTO = postService.updatePost(principal.name, postId, update)

    @DeleteMapping("/delete/{postId}")
    fun deletePost(@PathVariable("postId") postId: String, principal: Principal): CommonResponse {
        postService.deletePost(principal.name, postId)
        return CommonResponse("delete post success", "success", 200)
    }

    @GetMapping("")
    fun findAll(principal: Principal): List<PostReadDTO> = postService.findAll(principal.name)

    @GetMapping("/details/{postId}")
    fun findById(@PathVariable("postId") postId: String, principal: Principal): PostReadDTO =
        postService.findById(loginId = principal.name, postId = postId)

    @GetMapping("/user/{userId}")
    fun findByUserId(@PathVariable("userId") userId: String, principal: Principal): List<PostReadDTO> =
        postService.findAllByUserId(loginId = principal.name, userId = userId)

    @PostMapping("/favorite/{postId}")
    fun favoriteUp(principal: Principal, @PathVariable("postId") postId: String): Long =
        postService.favoriteUp(principal.name, postId = postId)
}


