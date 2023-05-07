package com.tianea.fimo.domain.follow.repository

import com.tianea.fimo.domain.follow.entity.Follow
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FollowRepository : JpaRepository<Follow, String>{
    fun existsByFollowerAndFollowee(follower :String, followee:String): Boolean
    fun deleteByFollowerAndFollowee(follower: String, followee: String)
    fun findAllByFollowee(followee: String) : List<Follow>
    fun findAllByFollower(follower:String) : List<Follow>
}