package com.sorsix.album_collector.repository

import com.sorsix.album_collector.domain.Event
import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository:JpaRepository<Event,Long>