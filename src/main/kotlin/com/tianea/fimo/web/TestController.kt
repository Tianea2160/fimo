package com.tianea.fimo.web

import com.tianea.fimo.domain.user.entity.User
import com.tianea.fimo.domain.user.repository.UserRepository
import com.tianea.fimo.shared.security.JwtService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/test")
@RestController
class TestController(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,

    ) {
    @GetMapping("/user")
    fun user(): String = "user"

    @GetMapping("/login")
    fun login(@RequestParam("userId") userId: String): String {
        if (!userRepository.existsById(userId)) {
            val user = User(id = userId, nickname = "test", archiveName = "test")
            userRepository.save(user)
        }
        return jwtService.createAccessToken(userId)
    }
}