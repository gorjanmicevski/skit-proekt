package com.sorsix.album_collector

import com.sorsix.album_collector.api.dtos.CollectorRegistration
import com.sorsix.album_collector.domain.Collector
import com.sorsix.album_collector.domain.ERole
import com.sorsix.album_collector.domain.Role
import com.sorsix.album_collector.repository.CollectorRepository
import com.sorsix.album_collector.repository.RoleRepository
import com.sorsix.album_collector.service.impl.CollectorService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

class CollectorServiceTest {

    val collectorRepository: CollectorRepository = mockk()
    val roleRepository: RoleRepository = mockk()
    val encoder: PasswordEncoder = mockk()
    val collectorService = CollectorService(collectorRepository, roleRepository, encoder)
    val collector = Collector(0,"Name","Surname","email@email.com","pass", hashSetOf(Role(1, ERole.ROLE_USER)))

    @Test
    fun whenCreateCollector_thenReturnCollector() {
        //given
        every { collectorRepository.save(collector) } returns collector
        every { encoder.encode(collector.password) } returns "pass"
        every { roleRepository.save(Role(id=1, name= ERole.ROLE_USER))} returns Role(1, ERole.ROLE_USER)
        every { roleRepository.findByName(ERole.ROLE_USER) } returns Role(1, ERole.ROLE_USER)
        //when
        val result = collectorService
            .createCollector(CollectorRegistration(collector.name,collector.surname,collector.email,collector.password))

        //then
        Assertions.assertEquals(collector, result)
        verify(exactly = 1) { collectorRepository.save(collector) }
    }

}