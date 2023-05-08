package com.tianea.fimo.web.follow

import com.tianea.fimo.domain.follow.service.FollowService
import com.tianea.fimo.domain.user.dto.ProfileReadDTO
import com.tianea.fimo.shared.dto.CommonResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.security.Principal

@Tag(name = "Follow", description = "팔로우/언팔로우, 팔로우한 사용자 조회 기능이 있습니다.")
@RestController
@RequestMapping("/api/v1/follow")
class FollowController(
    private val followService: FollowService
) {
    @Operation(
        summary = "자신이 팔로우 했거나, 나를 팔로우하는 사용자를 조회합니다.",
        description = "자신이 팔로우했거나, 내가 팔로우하거나, 서로 팔로우 하는 경우의 사용자를 조회합니다."
    )
    @GetMapping("/me")
    fun getMyFollows(principal: Principal): List<ProfileReadDTO> = followService.myFollowers(principal.name)

    @Operation(summary = "팔로우", description = "다른 사람을 팔로우 합니다. 이미 팔로우를 한 경우에는 성공을 반환합니다.")
    @PostMapping("/following/{followee}")
    fun follow(principal: Principal, @PathVariable("followee") followee: String): CommonResponse {
        followService.follow(principal.name, followee)
        return CommonResponse(code = "follow success", message = "follow success", status = 200)
    }

    @Operation(summary = "언팔로우", description = "팔로우한 사람을 언팔로우 합니다. 언팔로우가 되어 있거나 아무 관계가 아닌경우 그냥 성공을 반환합니다.")
    @PostMapping("/unfollowing/{followee}")
    fun unfollow(principal: Principal, @PathVariable("followee") followee: String): CommonResponse {
        followService.unfollow(principal.name, followee)
        return CommonResponse(code = "unfollow success", message = "unfollow success", status = 200)
    }
}