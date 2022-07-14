package com.sorsix.album_collector

import com.sorsix.album_collector.domain.Album
import com.sorsix.album_collector.repository.AlbumRepository
import com.sorsix.album_collector.repository.StickerRepository
import com.sorsix.album_collector.service.CsvService
import com.sorsix.album_collector.service.impl.AlbumService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class AlbumServiceTest {

    val albumRepository: AlbumRepository = mockk()
    val stickerRepository: StickerRepository = mockk()
    val csvService: CsvService = mockk()
    val albumService = AlbumService(albumRepository, stickerRepository, csvService)

    val album = Album(1, "Album", ByteArray(0), mutableListOf())
    val albums = listOf(album)

    @Test
    fun whenGetAlbums_thenReturnAlbums() {
        //given
        every { albumRepository.findAll() } returns albums

        //when
        val result = albumService.getAll()

        //then
        assertEquals(albums, result)
        verify(exactly = 1) { albumRepository.findAll() }
    }

    @Test
    fun whenGetStickersForAlbum_thenReturnStickersForAlbum() {
        //given
        every { albumRepository.findByIdOrNull(0) } returns album

        //when
        val result = albumService.getStickersForAlbum(0)

        //then
        assertEquals(album.stickers, result)
        verify(exactly = 1) { albumRepository.findByIdOrNull(0) }
    }

    @Test
    fun whenGetAlbumImage_thenReturnAlbumImage(){
        //given
        every { albumRepository.findByIdOrNull(0) } returns album

        //when
        val result = albumService.getAlbumImage(0)

        //then
        assertEquals(album.imageUrl, result)
        verify(exactly = 1) { albumRepository.findByIdOrNull(0) }
    }
}