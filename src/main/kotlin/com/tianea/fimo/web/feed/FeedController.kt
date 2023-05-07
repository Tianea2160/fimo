package com.tianea.fimo.web.feed

import com.tianea.fimo.domain.feed.service.FeedService
import com.tianea.fimo.domain.post.dto.PostReadDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/feed")
class FeedController(
    private val feedService: FeedService
) {
    @GetMapping("")
    fun feed(principal: Principal): List<PostReadDTO> = feedService.getFeed(principal.name)
}