package com.sorsix.album_collector

import com.sorsix.album_collector.domain.Collector
import com.sorsix.album_collector.domain.ERole
import com.sorsix.album_collector.domain.Role
import com.sorsix.album_collector.repository.CollectorRepository
import com.sorsix.album_collector.repository.PrivateAlbumInstanceRepository
import com.sorsix.album_collector.repository.StickerRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager


@DataJpaTest
class RepositoryTests {

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var collectorRepository: CollectorRepository

    @Autowired
    lateinit var stickerRepository: StickerRepository

    @Autowired
    lateinit var privateAlbumInstanceRepository: PrivateAlbumInstanceRepository

    final val role = Role(name=ERole.ROLE_USER)
    val collector = Collector(
        0,
        "Name",
        "Surname",
        "email@email.com",
        "pass",
        hashSetOf(role)
    )

    @Test
    fun whenFindByEmail_thenReturnCollector() {
        entityManager.persist(role)
        entityManager.persist(collector)
        entityManager.flush()
        val result = collectorRepository.findByEmail(collector.email)
        assertEquals(collector, result)
    }

    @Test
    fun whenCollectorExistByEmail_thenReturnTrue() {
        entityManager.persist(role)
        entityManager.persist(collector)
        entityManager.flush()
        val result = collectorRepository.existsByEmail(collector.email)
        assertTrue(result)
    }

}