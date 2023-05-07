package com.tianea.fimo.domain.auth.service

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val redisTemplate: StringRedisTemplate
) {
}