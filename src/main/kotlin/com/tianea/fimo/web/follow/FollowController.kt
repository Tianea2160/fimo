package com.tianea.fimo.web.follow

import com.tianea.fimo.domain.follow.dto.FollowReadDTO
import com.tianea.fimo.domain.follow.service.FollowService
import com.tianea.fimo.domain.user.dto.ProfileReadDTO
import com.tianea.fimo.web.post.CommonResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/follow")
class FollowController(
    private val followService: FollowService
) {
    @GetMapping("/me")
    fun getMyFollows(principal: Principal): List<ProfileReadDTO> = followService.myFollowers(principal.name)

    @PostMapping("/following/{followee}")
    fun follow(principal: Principal, @PathVariable("followee") followee: String): CommonResponse {
        followService.follow(principal.name, followee)
        return CommonResponse(code = "follow success", message = "follow success", status = 200)
    }
    @PostMapping("/unfollowing/{followee}")
    fun unfollow(principal: Principal, @PathVariable("followee") followee: String): CommonResponse {
        followService.unfollow(principal.name, followee)
        return CommonResponse(code = "unfollow success", message = "unfollow success", status = 200)
    }
}