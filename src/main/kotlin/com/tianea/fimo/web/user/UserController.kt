package com.tianea.fimo.web.user

import com.tianea.fimo.domain.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/user/validate/nickname")
    fun validateNickname(@RequestParam("nickname") nickname: String) = userService.validateNickname(nickname)
    @GetMapping("/user/validate/archive")
    fun validateArchiveName(@RequestParam("archive") archive: String) = userService.validateArchiveName(archive)
}