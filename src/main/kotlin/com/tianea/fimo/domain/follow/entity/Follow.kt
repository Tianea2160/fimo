package com.tianea.fimo.domain.follow.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "follows")
class Follow(
    @Id
    val id: String,
    val follower: String,
    val followee: String,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Follow) return false
        val f = other as Follow
        return (this.follower == f.follower && this.followee == f.followee) || (this.follower == f.followee && this.followee == f.follower)
    }

    override fun toString(): String =
        "Follow(id='$id', follower='$follower', followee='$followee', createdAt=$createdAt)"

    override fun hashCode(): Int = if (follower > followee)
        follower.hashCode() + followee.hashCode()
    else
        followee.hashCode() + follower.hashCode()
}