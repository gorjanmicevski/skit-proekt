package com.sorsix.album_collector.repository

import com.sorsix.album_collector.domain.Album
import org.springframework.data.jpa.repository.JpaRepository

interface AlbumRepository : JpaRepository<Album,Long>