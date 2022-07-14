package com.sorsix.album_collector.service.impl

import com.sorsix.album_collector.api.dtos.EventCreator
import com.sorsix.album_collector.domain.Collector
import com.sorsix.album_collector.domain.Event
import com.sorsix.album_collector.repository.AlbumRepository
import com.sorsix.album_collector.repository.CollectorRepository
import com.sorsix.album_collector.repository.EventRepository
import com.sorsix.album_collector.service.EventService
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class EventService(
    val eventRepository: EventRepository,
    val albumRepository: AlbumRepository,
    val collectorRepository: CollectorRepository
) : EventService {
    override fun createEvent(eventCreator: EventCreator): Event {
        val albums = albumRepository.findAllById(eventCreator.albumIds)
        return eventRepository.save(
            Event(
                title = eventCreator.title,
                location = eventCreator.location,
                albums = albums
            )
        )
    }

    override fun addCollectorGoing(eventId: Long, collectorId: Long) {
        val event: Event = eventRepository.findByIdOrNull(eventId)
            ?: throw EntityNotFoundException("Event with given id does not exist")
        val collector: Collector = collectorRepository.findByIdOrNull(collectorId)
            ?: throw EntityNotFoundException("Collector with given id does not exist")
        event.collectorsGoing.add(collector)
    }

    override fun addCollectorInterested(eventId: Long, collectorId: Long) {
        val event: Event = eventRepository.findByIdOrNull(eventId)
            ?: throw EntityNotFoundException("Event with given id does not exist")
        val collector: Collector = collectorRepository.findByIdOrNull(collectorId)
            ?: throw EntityNotFoundException("Collector with given id does not exist")
        event.collectorsInterested.add(collector)
    }

    override fun updateEvent(eventId: Long, eventCreator: EventCreator): Event {
        val event: Event = eventRepository.findByIdOrNull(eventId)
            ?: throw EntityNotFoundException("Event with given id does not exist")
        event.title = eventCreator.title
        event.location = eventCreator.location
        val albums = albumRepository.findAllById(eventCreator.albumIds)
        event.albums = albums
        return eventRepository.save(event)
    }

    override fun getAllPaginated(page: Int, pageSize: Int, albumIds: List<Long>?): List<Event> {
        return if (albumIds != null && albumRepository.findAllById(albumIds).size > 0)
            eventRepository.findAll(
                PageRequest.of(
                    page,
                    pageSize
                )
            ).content.filter { event -> event.albums.any { albumIds.contains(it.id) } }
                .sortedBy { it.collectorsGoing.size + it.collectorsInterested.size }
        else
            eventRepository.findAll(
                PageRequest.of(
                    page,
                    pageSize
                )
            ).content.sortedBy { it.collectorsGoing.size + it.collectorsInterested.size }
    }
}