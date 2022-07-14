package com.sorsix.album_collector.security.service

import com.sorsix.album_collector.domain.Collector
import com.sorsix.album_collector.repository.CollectorRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl(
    val collectorRepository: CollectorRepository
) : UserDetailsService {
    @Transactional
    override fun loadUserByUsername(username: String?): UserDetails {
        val collector: Collector = collectorRepository.findByEmail(username!!)
        return UserDetailsImpl.build(collector)
    }
}