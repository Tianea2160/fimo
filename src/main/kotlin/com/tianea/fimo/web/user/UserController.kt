package com.tianea.fimo.web.user

import com.tianea.fimo.domain.user.dto.MyProfileReadDTO
import com.tianea.fimo.domain.user.dto.ProfileReadDTO
import com.tianea.fimo.domain.user.dto.ProfileUpdateDTO
import com.tianea.fimo.domain.user.service.UserService
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

@Tag(name = "User", description = "프로필 조회, 프로필 수정, (사용자, 아카이브 이름) 중복 체크 기능이 있습니다.")
@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
) {
    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복 체크 기능입니다. 유효한 닉네임이면 true, 아니면 false를 반환합니다.")
    @GetMapping("/validate/nickname")
    fun validateNickname(@RequestParam("nickname") nickname: String) = userService.isValidNickname(nickname)

    @Operation(summary = "아카이브 이름 중복 체크", description = "아카이브 이름 중복 체크 기능입니다. 유효한 아카이브 이름이면 true, 아니면 false를 반환합니다.")
    @GetMapping("/validate/archive")
    fun validateArchiveName(@RequestParam("archive") archive: String) = userService.isValidArchiveName(archive)

    @Operation(summary = "프로필 수정", description = "프로필 수정 기능입니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "프로필 수정 성공"),
            ApiResponse(
                responseCode = "400",
                description = "프로필 수정 실패",
                content = [Content(schema = Schema(implementation = ErrorCode::class))]
            )
        ]
    )
    @PostMapping("/profile/update")
    fun updateProfile(principal: Principal, @RequestBody update: ProfileUpdateDTO) =
        userService.updateProfile(principal.name, update)

    @Operation(summary = "프로필 조회", description = "프로필 조회 기능입니다.")
    @GetMapping("/profile/me")
    fun getMyProfile(principal: Principal): MyProfileReadDTO = userService.getMyProfile(principal.name)

    @Operation(summary = "상대방 프로필 조회", description = "프로필 조회 기능입니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "프로필 조회 성공"),
            ApiResponse(
                responseCode = "400",
                description = "프로필 조회 실패",
                content = [Content(schema = Schema(implementation = ErrorCode::class))]
            )
        ]
    )
    @GetMapping("/profile/{userId}")
    fun getUserProfile(
        @PathVariable("userId") userId: String,
        principal: Principal
    ): ProfileReadDTO = userService.getUserProfile(principal.name, userId)

}