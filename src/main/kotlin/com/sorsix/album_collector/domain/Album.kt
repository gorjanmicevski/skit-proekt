package com.sorsix.album_collector.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Album(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    @JsonIgnore
    val imageUrl: ByteArray,
    @OneToMany(mappedBy = "album")
    val stickers: MutableList<Sticker> = mutableListOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Album

        if (!imageUrl.contentEquals(other.imageUrl)) return false

        return true
    }

    override fun hashCode(): Int {
        return imageUrl.contentHashCode()
    }
}