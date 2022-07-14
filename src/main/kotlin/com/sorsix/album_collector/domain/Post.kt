package com.sorsix.album_collector.domain

import com.fasterxml.jackson.annotation.JsonIncludeProperties
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Post(
    @Id @GeneratedValue val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY) @JsonIncludeProperties("name", "surname","id") val collector: Collector,
    var collectorName: String,
    var description: String,
    var phone: String,
    var location: String,
    @ManyToOne(fetch = FetchType.LAZY) @JsonIncludeProperties("id", "name") var album: Album,
    @Column(length=10485760) var duplicateStickers: String?,
    @Column(length=10485760) var missingStickers: String?,
    var imageDuplicatesStickers: ByteArray?,
    var imageMissingStickers: ByteArray?,
    val dateTimeCreated: LocalDateTime
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        if (!imageDuplicatesStickers.contentEquals(other.imageDuplicatesStickers)) return false
        if (!imageMissingStickers.contentEquals(other.imageMissingStickers)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = imageDuplicatesStickers.contentHashCode()
        result = 31 * result + imageMissingStickers.contentHashCode()
        return result
    }
}