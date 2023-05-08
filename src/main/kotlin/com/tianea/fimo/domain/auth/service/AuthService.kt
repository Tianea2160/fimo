package com.tianea.fimo.domain.auth.service

import com.tianea.fimo.domain.auth.dto.LoginDTO
import com.tianea.fimo.domain.auth.error.ArchiveAlreadyExistException
import com.tianea.fimo.domain.auth.error.JwtValidationException
import com.tianea.fimo.domain.auth.error.NicknameAlreadyExistException
import com.tianea.fimo.domain.auth.error.UserAlreadyExistException
import com.tianea.fimo.domain.user.dto.UserCreateDTO
import com.tianea.fimo.domain.user.error.UserNotFoundException
import com.tianea.fimo.domain.user.service.UserService
import com.tianea.fimo.shared.security.JwtService
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val redisTemplate: StringRedisTemplate,
    private val jwtService: JwtService,
    private val userService: UserService,
) {
    @Transactional
    fun login(login: LoginDTO): LoginSuccessDTO {
        val userId = "${login.provider}_${login.identifier}"
        if (userService.isValidUserId(userId))
            throw UserNotFoundException()
        val access = jwtService.createAccessToken(username = userId)
        val refresh = jwtService.createRefreshToken(username = userId)
        redisTemplate.opsForValue().set(userId, refresh)// redis save
        return LoginSuccessDTO(access, refresh)
    }

    @Transactional
    fun logout(loginId: String) {
        redisTemplate.delete(loginId)
    }

    @Transactional
    fun reissue(access: String, refresh: String): ReissueSuccessDTO {
        if (!jwtService.validateToken(access) || !jwtService.validateToken(refresh))
            throw JwtValidationException()
        val username = jwtService.getSubjectFromToken(access)
        if (redisTemplate.opsForValue().get(username) != refresh)
            throw JwtValidationException()

        val newAccess = jwtService.createAccessToken(username)
        val newRefresh = jwtService.createRefreshToken(username)
        redisTemplate.opsForValue().set(username, refresh)
        return ReissueSuccessDTO(newAccess, newRefresh)
    }

    @Transactional
    fun signUp(signUp: SignUpDTO) {
        val userId = "${signUp.provider}_${signUp.identifier}"
        if (!userService.isValidUserId(userId)) throw UserAlreadyExistException()
        if (!userService.isValidArchiveName(signUp.archiveName)) throw ArchiveAlreadyExistException()
        if (!userService.isValidNickname(signUp.nickname)) throw NicknameAlreadyExistException()

        val create = UserCreateDTO(
            id = userId,
            nickname = signUp.nickname,
            archiveName = signUp.archiveName,
            profileImageUrl = signUp.profileImageUrl
        )
        userService.createUser(create)
    }

    fun withdrawal(loginId: String) {
        userService.withdrawal(loginId)
    }
}

class ReissueSuccessDTO(
    val accessToken: String,
    val refreshToken: String,
)

class LoginSuccessDTO(
    val accessToken: String,
    val refreshToken: String,
)

class SignUpDTO(
    val provider: String,
    val identifier: String,
    val nickname: String,
    val archiveName: String,
    val profileImageUrl: String
)

