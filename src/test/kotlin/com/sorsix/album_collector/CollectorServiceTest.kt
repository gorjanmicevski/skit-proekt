package com.sorsix.album_collector

import com.sorsix.album_collector.api.dtos.CollectorRegistration
import com.sorsix.album_collector.api.dtos.CollectorUpdate
import com.sorsix.album_collector.domain.Collector
import com.sorsix.album_collector.domain.ERole
import com.sorsix.album_collector.domain.Role
import com.sorsix.album_collector.repository.CollectorRepository
import com.sorsix.album_collector.repository.RoleRepository
import com.sorsix.album_collector.service.impl.CollectorService
import io.mockk.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder

class CollectorServiceTest {

    val collectorRepository: CollectorRepository = mockk()
    val roleRepository: RoleRepository = mockk()
    val encoder: PasswordEncoder = mockk()
    val collectorService = CollectorService(collectorRepository, roleRepository, encoder)
    val collector = Collector(0, "Name", "Surname", "email@email.com", "pass", hashSetOf(Role(1, ERole.ROLE_USER)))

    @Test
    fun whenCreateCollector_thenReturnCreatedCollector() {
        //given
        every { collectorRepository.save(collector) } returns collector
        every { encoder.encode(collector.password) } returns "pass"
        every { roleRepository.save(Role(id = 1, name = ERole.ROLE_USER)) } returns Role(1, ERole.ROLE_USER)
        every { roleRepository.findByName(ERole.ROLE_USER) } returns Role(1, ERole.ROLE_USER)
        //when
        val result = collectorService
            .createCollector(
                CollectorRegistration(
                    collector.name,
                    collector.surname,
                    collector.email,
                    collector.password
                )
            )

        //then
        Assertions.assertEquals(collector, result)
        verifyOrder {
            encoder.encode(collector.password)
            roleRepository.save(Role(id = 1, name = ERole.ROLE_USER))
            roleRepository.findByName(ERole.ROLE_USER)
            collectorRepository.save(collector)
        }
    }


    @Test
    fun whenGetProfilePicture_thenReturnProfilePicture() {
        //given
        every { collectorRepository.findByIdOrNull(0) } returns collector
        //when
        val result = collectorService.getProfilePicture(0)
        //then
        Assertions.assertEquals(collector.profilePicture, result)
        verify(exactly = 1) { collectorRepository.findByIdOrNull(0) }
    }

    @Test
    fun whenEmailIsTaken_thenReturnTrue() {
        //given
        every { collectorRepository.existsByEmail("email@email.com") } returns true
        //when
        val result = collectorService.emailTaken("email@email.com")
        //then
        Assertions.assertTrue(result)
        verify(exactly = 1) { collectorRepository.existsByEmail("email@email.com") }
    }

    @Test
    fun whenGetCollector_thenReturnCollector() {
        //given
        every { collectorRepository.findByIdOrNull(0) } returns collector
        //when
        val result = collectorService.getCollector(0)
        //then
        Assertions.assertEquals(collector, result)
        verify(exactly = 1) { collectorRepository.findByIdOrNull(0) }
    }

    @Test
    fun whenUpdateCollector_thenReturnUpdatedCollector(){
        val updatedCollector = Collector(0,"updatedName","updatedSurname","update@update.com","updatedPass")
        //given
        every { collectorRepository.findByIdOrNull(0) } returns collector
        every { collectorRepository.save(updatedCollector) } returns updatedCollector
        //when
        val result = collectorService
            .updateCollector(CollectorUpdate(0,"updatedName","updatedSurname","update@update.com","updatedPass"))
        //then
        Assertions.assertEquals(updatedCollector, result)
        verify(exactly = 1) { collectorRepository.findByIdOrNull(0) }
    }
}