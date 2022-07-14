package com.sorsix.album_collector.api.controllers

import com.sorsix.album_collector.domain.Album
import com.sorsix.album_collector.domain.Sticker
import com.sorsix.album_collector.service.AlbumService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/albums")
@CrossOrigin(origins = ["http://localhost:4200"])
class AlbumsController(
    val albumService: AlbumService
) {
    //Albums

    @GetMapping()
    fun getAlbums() = albumService.getAll()

    @GetMapping("/{albumId}/stickers")
    fun getStickers(@PathVariable albumId: Long): List<Sticker> {
        return albumService.getStickersForAlbum(albumId)
    }

    @PostMapping("/import")
    fun uploadAlbum(
        @RequestParam file: MultipartFile, @RequestParam name: String, @RequestParam image: MultipartFile
    ): ResponseEntity<Album> {
        val album = albumService.importStickers(file, name, image)
        return ResponseEntity.ok(album)
    }

    @GetMapping("/{albumId}/image")
    fun getAlbumImage(@PathVariable albumId: Long): ResponseEntity<Any> {
        val image: ByteArray = albumService.getAlbumImage(albumId)
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${System.currentTimeMillis()}\"")
            .body(image)
    }

}