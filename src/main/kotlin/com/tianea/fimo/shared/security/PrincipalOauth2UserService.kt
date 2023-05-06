package com.tianea.fimo.shared.security

import com.tianea.fimo.domain.user.entity.User
import com.tianea.fimo.domain.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service


@Service
class PrincipalOauth2UserService(
    private val userRepository: UserRepository
) : DefaultOAuth2UserService() {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val user = super.loadUser(userRequest)
        val provider = userRequest.clientRegistration.registrationId
        val userInfo = when(provider){
            "kakao"-> KakaoOAuth2UserInfo(user.attributes)
            else -> throw IllegalArgumentException("지원하지 않는 소셜 로그인입니다.")
        }
        val id = userInfo.getProvider() + "_" + userInfo.getProviderId()

        // 만약 없는 사용자라면 사용자를 새롭게 생성하고 있는 사용자라면 사용자 정보를
        logger.info("id: $id")
        val isNew = !userRepository.existsById(id)
        if(isNew){
            logger.info("새로운 사용자 생성")
            userRepository.save(User(id = id))
        }
        return UserDetails(id, userInfo, isNew)
    }
}

interface OAuth2UserInfo {
    fun getProviderId(): String
    fun getProvider(): String
}

class KakaoOAuth2UserInfo(private val attributes: Map<String, Any>) : OAuth2UserInfo {
    override fun getProviderId(): String = attributes["id"].toString()
    override fun getProvider(): String = "kakao"
}

class UserDetails(
    private val username : String,
    private val userInfo : OAuth2UserInfo,
    val isNew : Boolean
) : OAuth2User {
    override fun getName(): String = username
    override fun getAttributes(): MutableMap<String, Any> = mutableMapOf()
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority("USER"))
}