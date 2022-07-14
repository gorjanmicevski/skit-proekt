package com.sorsix.album_collector.api.controllers

import com.sorsix.album_collector.api.dtos.EventCreator
import com.sorsix.album_collector.domain.Event
import com.sorsix.album_collector.service.EventService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = ["http://localhost:4200"])
class EventsController(
    val eventService: EventService
) {
    @PostMapping("/create")
    fun createEvent(@RequestBody eventCreator: EventCreator): ResponseEntity<Event> {
        return ResponseEntity.ok(eventService.createEvent(eventCreator))
    }

    @PutMapping("/{eventId}/addCollectorGoing/{collectorId}")
    fun addCollectorGoing(@PathVariable eventId: Long, @PathVariable collectorId: Long) {
        eventService.addCollectorGoing(eventId, collectorId)
    }

    @PutMapping("/{eventId}/addCollectorInterested/{collectorId}")
    fun addCollectorInterested(@PathVariable eventId: Long, @PathVariable collectorId: Long) {
        eventService.addCollectorInterested(eventId, collectorId)
    }

    @PutMapping("/{eventId}/update")
    fun updateEvent(@PathVariable eventId: Long, @RequestBody eventCreator: EventCreator): ResponseEntity<Event> {
        return ResponseEntity.ok(eventService.updateEvent(eventId = eventId, eventCreator = eventCreator))
    }

    @GetMapping
    fun getEventsPaginated(@RequestParam page: Int, @RequestParam pageSize: Int, @RequestParam albumIds: List<Long>) {

    }
}