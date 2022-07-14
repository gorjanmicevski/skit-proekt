package com.sorsix.album_collector.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIncludeProperties
import javax.persistence.*

@Entity
data class PrivateAlbumInstance(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val Id: Long = 0,
    @ManyToOne
    @JsonIncludeProperties("name", "surname")
    val collector: Collector,
    @ManyToOne
    @JsonIncludeProperties("id", "name")
    val album: Album,
    @OneToMany
    @JsonIgnore
    val collectedStickers: MutableList<Sticker>,
    @OneToMany
    @JsonIgnore
    val duplicateStickers: MutableList<Sticker>
)
