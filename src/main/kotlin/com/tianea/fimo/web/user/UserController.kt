package com.tianea.fimo.web.user

import com.tianea.fimo.domain.user.dto.MyProfileReadDTO
import com.tianea.fimo.domain.user.dto.ProfileReadDTO
import com.tianea.fimo.domain.user.dto.ProfileUpdateDTO
import com.tianea.fimo.domain.user.service.UserService
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/validate/nickname")
    fun validateNickname(@RequestParam("nickname") nickname: String) = userService.validateNickname(nickname)

    @GetMapping("/validate/archive")
    fun validateArchiveName(@RequestParam("archive") archive: String) = userService.validateArchiveName(archive)

    @PostMapping("/profile/update")
    fun updateProfile(principal: Principal, @RequestBody update: ProfileUpdateDTO) =
        userService.updateProfile(principal.name, update)

    @GetMapping("/profile/me")
    fun getMyProfile(principal: Principal): MyProfileReadDTO = userService.getMyProfile(principal.name)

    @GetMapping("/profile/{userId}")
    fun getUserProfile(
        @PathVariable("userId") userId: String,
        principal: Principal
    ): ProfileReadDTO = userService.getUserProfile(principal.name, userId)
}