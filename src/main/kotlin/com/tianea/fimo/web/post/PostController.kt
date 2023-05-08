package com.tianea.fimo.web.post

import com.tianea.fimo.domain.post.dto.PostCreateDTO
import com.tianea.fimo.domain.post.dto.PostReadDTO
import com.tianea.fimo.domain.post.dto.PostUpdateDTO
import com.tianea.fimo.domain.post.service.PostService
import com.tianea.fimo.shared.dto.CommonResponse
import com.tianea.fimo.shared.error.ErrorCode
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.security.Principal


@Tag(name = "Post", description = "게시글 생성, 수정, 삭제, 조회(단건, 전체, 특정 사용자) 등의 기능이 있습니다.")
@RestController
@RequestMapping("/api/v1/post")
class PostController(
    private val postService: PostService

) {
    @Operation(summary = "게시글 생성", description = "게시글을 생성합니다.")
    @PostMapping("/create")
    fun createPost(
        principal: Principal,
        @RequestBody create: PostCreateDTO
    ): PostReadDTO = postService.createPost(principal.name, create)

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            ApiResponse(
                responseCode = "404",
                description = "게시글 수정 실패",
                content = [Content(schema = Schema(implementation = ErrorCode::class))]
            )]
    )
    @PutMapping("/update/{postId}")
    fun updatePost(
        @PathVariable("postId")
        postId: String,
        principal: Principal,
        @RequestBody update: PostUpdateDTO
    ): PostReadDTO = postService.updatePost(principal.name, postId, update)

    @DeleteMapping("/delete/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    fun deletePost(@PathVariable("postId") postId: String, principal: Principal): CommonResponse {
        postService.deletePost(principal.name, postId)
        return CommonResponse("delete post success", "success", 200)
    }

    @GetMapping("")
    @Operation(summary = "게시글 전체 조회", description = "모든 게시글을 조회합니다.")
    fun findAll(principal: Principal): List<PostReadDTO> = postService.findAll(principal.name)

    @GetMapping("/details/{postId}")
    @Operation(summary = "게시글 단건 조회", description = "게시글을 단건 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "게시글 단건 조회 성공"),
            ApiResponse(
                responseCode = "404",
                description = "게시글 단건 조회 실패",
                content = [Content(schema = Schema(implementation = ErrorCode::class))]
            )]
    )
    fun findById(@PathVariable("postId") postId: String, principal: Principal): PostReadDTO =
        postService.findById(loginId = principal.name, postId = postId)

    @GetMapping("/user/{userId}")
    @Operation(summary = "특정 사용자의 게시글 조회", description = "특정 사용자의 게시글을 조회합니다.")
    fun findByUserId(@PathVariable("userId") userId: String, principal: Principal): List<PostReadDTO> =
        postService.findAllByUserId(loginId = principal.name, userId = userId)

    @PostMapping("/favorite/{postId}")
    @Operation(summary = "게시글 좋아요", description = "게시글에 좋아요를 합니다. 좋아요는 여러번 누를 수 있고 누를 때마다 좋아요 수가 증가합니다. 증가된 값을 반환합니다.")
    fun favoriteUp(principal: Principal, @PathVariable("postId") postId: String): Long =
        postService.favoriteUp(principal.name, postId = postId)
}


