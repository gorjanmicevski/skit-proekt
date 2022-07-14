package com.sorsix.album_collector.service

import com.sorsix.album_collector.api.dtos.EventCreator
import com.sorsix.album_collector.domain.Event

interface EventService {
    fun createEvent(eventCreator: EventCreator): Event
    fun addCollectorGoing(eventId: Long, collectorId: Long)
    fun addCollectorInterested(eventId: Long, collectorId: Long)
    fun updateEvent(eventId: Long, eventCreator: EventCreator): Event
    fun getAllPaginated(page: Int, pageSize: Int, albumIds: List<Long>?): List<Event>
}