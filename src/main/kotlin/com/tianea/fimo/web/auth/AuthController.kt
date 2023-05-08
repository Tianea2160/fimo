package com.tianea.fimo.web.auth

import com.tianea.fimo.domain.auth.dto.LoginDTO
import com.tianea.fimo.domain.auth.service.AuthService
import com.tianea.fimo.domain.auth.service.LoginSuccessDTO
import com.tianea.fimo.domain.auth.service.ReissueSuccessDTO
import com.tianea.fimo.domain.auth.service.SignUpDTO
import com.tianea.fimo.shared.dto.CommonResponse
import com.tianea.fimo.shared.error.ErrorCode
import com.tianea.fimo.web.auth.dto.ReissueRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.security.Principal

@Tag(name = "Auth", description = "로그인/로그아웃, 토큰갱신, 회원가입/회원탈퇴와 같은 기능이 있습니다.")
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @Operation(
        summary = "로그인",
        description = "로그인을 시도합니다. \n" +
                "만약 해당하는 사용자가 없으면 404에러를 발생합니다. \n" +
                "그때는 회원가입을 시도해주세요."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "로그인 성공"),
            ApiResponse(
                responseCode = "404", description = "회원 가입이 되지 않은 경우",
                content = arrayOf(
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorCode::class)
                    )
                )
            )
        ]
    )
    @PostMapping("/login")
    fun login(@RequestBody login: LoginDTO): LoginSuccessDTO = authService.login(login)

    @Operation(summary = "로그아웃", description = "로그아웃을 시도합니다. 서버의 레디스에 저장되어 있는 사용자 정보를 삭제합니다.")
    @PostMapping("/logout")
    fun logout(principal: Principal): CommonResponse {
        authService.logout(principal.name)
        return CommonResponse(
            code = "SUCCESS",
            message = "로그아웃 성공",
            status = 200
        )
    }

    @Operation(
        summary = "회원가입",
        description = "회원가입을 시도합니다. \n" +
                "만약 이미 해당하는 사용자가 있으면 409에러를 발생합니다. \n" +
                "그때는 로그인을 시도해주세요."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "회원 가입 성공"),
            ApiResponse(
                responseCode = "409", description = "이미 해당하는 사용자가 있는 경우 충돌 예외를 발생",
                content = arrayOf(
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorCode::class)
                    )
                )
            )
        ]
    )
    @PostMapping("/signup")
    fun signUp(@RequestBody signUp: SignUpDTO): CommonResponse {
        authService.signUp(signUp)
        return CommonResponse(
            code = "SUCCESS",
            message = "회원가입 성공",
            status = 200
        )
    }

    @Operation(
        summary = "회원탈퇴", description = "회원탈퇴를 시도합니다. " +
                "해당 사용자와 관련되어 있는 모든 정보를 그 즉시 삭제됩니다." +
                "해당 api는 bearer 토큰을 첨부해야합니다."
    )
    @DeleteMapping("/withdrawal")
    fun signOut(principal: Principal): CommonResponse {
        authService.withdrawal(principal.name)
        return CommonResponse("withdrawal success", "withdrawal.success", 200)
    }

    @Operation(summary = "토큰갱신", description = "토큰갱신을 시도합니다.")
    @PostMapping("/reissue")
    fun reissue(@RequestBody reissue: ReissueRequest): ReissueSuccessDTO =
        authService.reissue(reissue.accessToken, reissue.refreshToken)
}

