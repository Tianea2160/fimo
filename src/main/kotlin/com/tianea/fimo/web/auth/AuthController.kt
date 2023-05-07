package com.tianea.fimo.web.auth

import com.tianea.fimo.domain.auth.service.AuthService
import com.tianea.fimo.domain.auth.service.ReissueSuccessDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/reissue")
    fun reissue(@RequestBody reissue: ReissueRequest): ReissueSuccessDTO =
        authService.reissue(reissue.accessToken, reissue.refreshToken)
}

class ReissueRequest(
    val accessToken: String,
    val refreshToken: String
)


