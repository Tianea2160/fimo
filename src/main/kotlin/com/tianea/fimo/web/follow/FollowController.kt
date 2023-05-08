package com.tianea.fimo.web.follow

import com.tianea.fimo.domain.follow.service.FollowService
import com.tianea.fimo.domain.user.dto.ProfileReadDTO
import com.tianea.fimo.shared.dto.CommonResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.security.Principal

@Tag(name = "Follow", description = "팔로우/언팔로우, 팔로우한 사용자 조회 기능이 있습니다.")
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