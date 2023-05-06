package com.tianea.fimo.shared.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class Oauth2AuthenticationSuccessHandler(
    private val jwtService: JwtService,
    private val objectMapper: ObjectMapper,
) : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val userDetails = authentication.principal as UserDetails
        val loginToken = LoginSuccessDTO(
            accessToken = jwtService.createAccessToken(userDetails.name),
            refreshToken = jwtService.createRefreshToken(userDetails.name),
            isNew = userDetails.isNew
        )
        response.writer.write(objectMapper.writeValueAsString(loginToken))
        response.contentType = "application/json"
    }
}

class LoginSuccessDTO(
    val accessToken: String,
    val refreshToken: String,
    val isNew : Boolean
)