package com.tianea.fimo.shared.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {
    private val bearer = "Bearer "
    private val authorization = "Authorization"
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        request.getHeader(authorization)?.let { header ->
            if (header.startsWith(bearer)) {
                val token = header.substring(bearer.length)
                if (jwtService.validateToken(token)) {
                    val authentication = jwtService.getAuthentication(token)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
            filterChain.doFilter(request, response)
        }
    }
}