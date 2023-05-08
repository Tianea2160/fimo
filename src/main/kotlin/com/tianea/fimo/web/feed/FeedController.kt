package com.tianea.fimo.web.feed

import com.tianea.fimo.domain.feed.service.FeedService
import com.tianea.fimo.domain.post.dto.PostReadDTO
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Tag(name = "Feed", description = "자신과 팔로우한 사용자의 게시글을 조회하는 기능이 있습니다.")
@RestController
@RequestMapping("/api/v1/feed")
class FeedController(
    private val feedService: FeedService
) {
    @GetMapping("")
    fun feed(principal: Principal): List<PostReadDTO> = feedService.getFeed(principal.name)
}