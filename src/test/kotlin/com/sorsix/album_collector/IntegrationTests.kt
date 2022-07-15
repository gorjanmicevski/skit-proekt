package com.sorsix.album_collector

import com.sorsix.album_collector.api.controllers.CollectorsController
import com.sorsix.album_collector.api.dtos.CollectorRegistration
import com.sorsix.album_collector.api.dtos.JwtResponse
import com.sorsix.album_collector.api.dtos.LoginRequest
import com.sorsix.album_collector.domain.Album
import com.sorsix.album_collector.domain.Collector
import com.sorsix.album_collector.service.impl.CollectorService
import io.mockk.every
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(
    classes = arrayOf(AlbumCollectorApplication::class),
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class IntegrationTests() {
    @Autowired
    lateinit var collectorService: CollectorService
    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun whenGetAllAlbums_thenReturnOk() {
        val result = restTemplate.getForEntity("/api/albums", List::class.java)
        assertEquals(HttpStatus.OK, result.statusCode)
    }

    @Test
    fun whenLoginSuccess_thenReturnJwtResponse() {
        collectorService.createCollector(CollectorRegistration("name","surname","email@email.com","pass"))
        val result = restTemplate.postForEntity(
            "/api/collectors/login",
            LoginRequest("email@email.com", "pass"),
            JwtResponse::class.java
        )
        assertEquals("email@email.com",result.body?.email)
    }

    @Test
    fun whenRegisterCollector_thenReturnCollector() {
        val result = restTemplate.postForEntity(
            "/api/collectors/registerCollector",
            CollectorRegistration("name","string","email1@email.com","password"),
            Any::class.java
        )
        assertEquals(HttpStatus.OK,result.statusCode)
    }
}