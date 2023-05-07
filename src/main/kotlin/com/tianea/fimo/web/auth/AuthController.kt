package com.tianea.fimo.web.auth

import com.tianea.fimo.domain.auth.dto.LoginDTO
import com.tianea.fimo.domain.auth.service.AuthService
import com.tianea.fimo.domain.auth.service.LoginSuccessDTO
import com.tianea.fimo.domain.auth.service.ReissueSuccessDTO
import com.tianea.fimo.domain.auth.service.SignUpDTO
import com.tianea.fimo.shared.dto.CommonResponse
import com.tianea.fimo.web.auth.dto.ReissueRequest
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun login(@RequestBody login: LoginDTO): LoginSuccessDTO = authService.login(login)

    @PostMapping("/logout")
    fun logout(principal: Principal): CommonResponse {
        authService.logout(principal.name)
        return CommonResponse(
            code = "SUCCESS",
            message = "로그아웃 성공",
            status = 200
        )
    }

    @PostMapping("/signup")
    fun signUp(@RequestBody signUp: SignUpDTO): CommonResponse {
        authService.signUp(signUp)
        return CommonResponse(
            code = "SUCCESS",
            message = "회원가입 성공",
            status = 200
        )
    }

    @DeleteMapping("/withdrawal")
    fun signOut(principal: Principal): CommonResponse {
        authService.withdrawal(principal.name)
        return CommonResponse("withdrawal success", "withdrawal.success", 200)
    }

    @PostMapping("/reissue")
    fun reissue(@RequestBody reissue: ReissueRequest): ReissueSuccessDTO =
        authService.reissue(reissue.accessToken, reissue.refreshToken)
}

