package com.sorsix.album_collector.repository

import com.sorsix.album_collector.domain.Album
import com.sorsix.album_collector.domain.Collector
import com.sorsix.album_collector.domain.PrivateAlbumInstance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PrivateAlbumInstanceRepository : JpaRepository<PrivateAlbumInstance, Long> {
    fun findByCollectorAndAlbum(collector: Collector, album: Album): PrivateAlbumInstance?
}