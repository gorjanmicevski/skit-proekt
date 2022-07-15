package com.sorsix.album_collector

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.sorsix.album_collector.api.dtos.CollectorRegistration
import com.sorsix.album_collector.domain.*
import com.sorsix.album_collector.security.jwt.AuthEntryPointJwt
import com.sorsix.album_collector.security.jwt.JwtUtils
import com.sorsix.album_collector.security.service.UserDetailsServiceImpl
import com.sorsix.album_collector.service.EventService
import com.sorsix.album_collector.service.impl.AlbumService
import com.sorsix.album_collector.service.impl.CollectorService
import com.sorsix.album_collector.service.impl.PostService
import com.sorsix.album_collector.service.impl.PrivateAlbumInstanceService
import io.mockk.every
import org.apache.catalina.mapper.Mapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@WebMvcTest
class ControllerTests(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    lateinit var albumsService: AlbumService

    @MockkBean
    lateinit var postService: PostService

    @MockkBean
    lateinit var collectorService: CollectorService

    @MockkBean
    lateinit var userDetailsServiceImpl: UserDetailsServiceImpl

    @MockkBean
    lateinit var authenticationManager: AuthenticationManager

    @MockkBean
    lateinit var jwtUtils: JwtUtils

    @MockkBean
    lateinit var eventService: EventService

    @MockkBean
    lateinit var privateAlbumInstance: PrivateAlbumInstanceService

    @MockkBean
    lateinit var authEntryPointJwt: AuthEntryPointJwt

    val album = Album(1, "Album", ByteArray(0), mutableListOf())
    val albums = listOf(album)
    val collector = Collector(0, "Name", "Surname", "email@email.com", "pass", hashSetOf(Role(1, ERole.ROLE_USER)))
    val post = Post(
        0, collector, collector.name, "descrtiption", "070 xxx xxx", "location", album, null, null, null, null,
        LocalDateTime.now()
    )
    val posts = listOf(post)

    @Test
    fun whenGetAlbums_thenReturnAlbums() {
        every { albumsService.getAll() } returns albums

        mockMvc.perform(get("/api/albums"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun whenGetStickersForValidAlbumId_thenReturnStickers() {
        every { albumsService.getStickersForAlbum(0) } returns album.stickers

        mockMvc.perform(get("/api/albums/0/stickers"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun whenGetStickersWithoutAlbumId_thenReturnBadRequest() {
        every { albumsService.getStickersForAlbum(0) } returns album.stickers

        mockMvc.perform(get("/api/albums/stickers"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun whenGetPostsPaginated_thenReturnPostsPaginated() {
        every { postService.getAllPaginated(0, 2, null) } returns posts

        mockMvc.perform(get("/api/posts?page=0&pageSize=2"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun whenRegisterValidCollector_thenReturnOk() {
        every {
            collectorService.createCollector(
                CollectorRegistration(
                    collector.name,
                    collector.surname,
                    collector.email,
                    collector.password
                )
            )
        } returns collector
        every { collectorService.emailTaken("email@email.com") } returns false

        val collectorRegistration =
            CollectorRegistration(collector.name, collector.surname, collector.email, collector.password)

        mockMvc.perform(
            post("/api/collectors/registerCollector").content(
                jacksonObjectMapper().writeValueAsString(
                    collectorRegistration
                )
            ).contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("Name"))
    }

    @Test
    fun whenRegisterInvalidCollector_thenReturnBadRequest() {
        every {
            collectorService.createCollector(
                CollectorRegistration(
                    collector.name,
                    collector.surname,
                    collector.email,
                    collector.password
                )
            )
        } returns collector
        every { collectorService.emailTaken("email@email.com") } returns true

        val collectorRegistration =
            CollectorRegistration(collector.name, collector.surname, collector.email, collector.password)

        mockMvc.perform(
            post("/api/collectors/registerCollector").content(
                jacksonObjectMapper().writeValueAsString(
                    collectorRegistration
                )
            ).contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType("text/plain;charset=UTF-8"))
            .andExpect(content().encoding(charset("UTF-8")))
            .andExpect(content().string("Error: Email already used"))
    }
}