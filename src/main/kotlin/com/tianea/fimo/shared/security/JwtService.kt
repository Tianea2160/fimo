package com.tianea.fimo.shared.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val secretKey: String,
) {
    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())
    private val accessDuration: Long = 1000 * 60 * 60 * 6
    private val refreshDuration: Long = 1000 * 60 * 60 * 24 * 7

    fun generateToken(username: String, time: Long): String = Jwts.builder().signWith(key)
        .setSubject(username)
        .setExpiration(Date(System.currentTimeMillis() + time))
        .compact()

    fun createAccessToken(username: String): String = generateToken(username, accessDuration)
    fun createRefreshToken(username: String): String = generateToken(username, refreshDuration)
    fun validateToken(token: String): Boolean = try {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
        true
    } catch (e: Exception) {
        false
    }

    fun getAuthentication(token: String): Authentication {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        return UsernamePasswordAuthenticationToken(claims.subject, null, listOf(SimpleGrantedAuthority("ROLE_USER")))
    }
}