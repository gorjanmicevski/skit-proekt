package com.sorsix.album_collector.repository

import com.sorsix.album_collector.domain.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {
    fun findByAlbum_Id(albumId: Long, pageable: Pageable): Page<Post>
}