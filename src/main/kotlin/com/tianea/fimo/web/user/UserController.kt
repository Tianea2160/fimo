package com.tianea.fimo.web.user

import com.tianea.fimo.domain.user.dto.MyProfileReadDTO
import com.tianea.fimo.domain.user.dto.ProfileReadDTO
import com.tianea.fimo.domain.user.dto.ProfileUpdateDTO
import com.tianea.fimo.domain.user.service.UserService
import com.tianea.fimo.shared.dto.CommonResponse
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/validate/nickname")
    fun validateNickname(@RequestParam("nickname") nickname: String) = userService.isValidNickname(nickname)

    @GetMapping("/validate/archive")
    fun validateArchiveName(@RequestParam("archive") archive: String) = userService.isValidArchiveName(archive)

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

    @DeleteMapping("/signout")
    fun signOut(principal: Principal): CommonResponse {
        userService.signOut(principal.name)
        return CommonResponse("sign out success", "sign.out.success", 200)
    }
}