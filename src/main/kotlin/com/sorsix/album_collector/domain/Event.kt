package com.sorsix.album_collector.domain

import com.fasterxml.jackson.annotation.JsonIncludeProperties
import javax.persistence.*

@Entity
data class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var title: String,
    var location: String,
    @OneToMany
    @JsonIncludeProperties("id", "name")
    var albums: List<Album>,
    @OneToMany
    @JsonIncludeProperties("id", "name", "surname")
    val collectorsGoing: MutableList<Collector> = mutableListOf(),
    @OneToMany
    @JsonIncludeProperties("id", "name", "surname")
    val collectorsInterested: MutableList<Collector> = mutableListOf()
)