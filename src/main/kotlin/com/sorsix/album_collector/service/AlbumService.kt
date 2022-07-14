package com.sorsix.album_collector.service

import com.sorsix.album_collector.domain.Album
import com.sorsix.album_collector.domain.Sticker
import org.springframework.web.multipart.MultipartFile

interface AlbumService {
    fun getAll(): List<Album>
    fun importStickers(file: MultipartFile, name: String, image: MultipartFile): Album
    fun getStickersForAlbum(albumId: Long): List<Sticker>
    fun getAlbumImage(albumId: Long):ByteArray
}