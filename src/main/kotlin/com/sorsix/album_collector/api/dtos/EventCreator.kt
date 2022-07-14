package com.sorsix.album_collector.api.dtos

data class EventCreator(
    val title: String,
    val location: String,
    val albumIds: List<Long>
)
