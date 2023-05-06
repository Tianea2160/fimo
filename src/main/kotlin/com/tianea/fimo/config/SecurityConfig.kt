package com.tianea.fimo.config

import com.tianea.fimo.shared.security.JwtAuthenticationFilter
import com.tianea.fimo.shared.security.Oauth2AuthenticationSuccessHandler
import com.tianea.fimo.shared.security.PrincipalOauth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfig(
    private val userService: PrincipalOauth2UserService,
    private val authenticationSuccessHandler: Oauth2AuthenticationSuccessHandler,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests()
            .requestMatchers("/login/**").permitAll()
            .requestMatchers("/test/user").hasRole("USER")
            .and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()

        http.oauth2Login()
            .successHandler(authenticationSuccessHandler)
            .userInfoEndpoint()
            .userService(userService)
        return http.build()
    }
}