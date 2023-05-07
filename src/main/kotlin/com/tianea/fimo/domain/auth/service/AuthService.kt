package com.tianea.fimo.domain.auth.service

import com.tianea.fimo.domain.auth.error.JwtValidationException
import com.tianea.fimo.shared.security.JwtService
import com.tianea.fimo.shared.security.LoginSuccessDTO
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val redisTemplate: StringRedisTemplate,
    private val jwtService: JwtService
) {
    fun reissue(access: String, refresh: String): ReissueSuccessDTO {
        if (!jwtService.validateToken(access) || !jwtService.validateToken(refresh))
            throw JwtValidationException()
        val username = jwtService.getSubjectFromToken(access)
        if (redisTemplate.opsForValue().get(username) != refresh)
            throw JwtValidationException()

        val newAccess = jwtService.createAccessToken(username)
        val newRefresh  = jwtService.createRefreshToken(username)
        return ReissueSuccessDTO(newAccess, newRefresh)
    }
}

class ReissueSuccessDTO(
    val accessToken: String,
    val refreshToken: String,
)
