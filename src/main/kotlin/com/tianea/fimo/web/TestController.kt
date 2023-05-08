package com.tianea.fimo.web

import com.tianea.fimo.domain.user.entity.User
import com.tianea.fimo.domain.user.repository.UserRepository
import com.tianea.fimo.shared.security.JwtService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@Tag(name = "Test", description = "개발에 사용되는 테스트 api 입니다. 프론트 개발을 하실때에 보실 필요가 없습니다.")
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