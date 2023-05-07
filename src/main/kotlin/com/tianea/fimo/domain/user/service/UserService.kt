package com.tianea.fimo.domain.user.service

import com.tianea.fimo.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun validateNickname(nickname: String) = !userRepository.existsByNickname(nickname)
    @Transactional(readOnly = true)
    fun validateArchiveName(archiveName: String) = !userRepository.existsByArchiveName(archiveName)
}