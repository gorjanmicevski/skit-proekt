package com.sorsix.album_collector.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIncludeProperties
import javax.persistence.*

@Entity
data class Sticker(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val number: String,
    val page: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIncludeProperties("name")
    val album: Album
)