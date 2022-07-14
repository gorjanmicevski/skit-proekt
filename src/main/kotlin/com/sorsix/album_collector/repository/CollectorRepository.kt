package com.sorsix.album_collector.repository

import com.sorsix.album_collector.domain.Collector
import org.springframework.data.jpa.repository.JpaRepository

interface CollectorRepository : JpaRepository<Collector, Long> {
    fun findByEmail(email: String): Collector
    fun existsByEmail(email: String): Boolean
}