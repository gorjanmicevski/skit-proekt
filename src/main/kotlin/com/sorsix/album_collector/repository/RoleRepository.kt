package com.sorsix.album_collector.repository

import com.sorsix.album_collector.domain.ERole
import com.sorsix.album_collector.domain.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: ERole): Role?
}