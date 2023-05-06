package com.tianea.fimo.domain.user.repository

import com.tianea.fimo.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User,String> {
    fun existsByNickname(nickname: String): Boolean
    fun existsByArchiveName(nickname: String) : Boolean
}