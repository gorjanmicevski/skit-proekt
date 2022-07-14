package com.sorsix.album_collector.service.impl

import com.sorsix.album_collector.api.dtos.CollectorRegistration
import com.sorsix.album_collector.api.dtos.CollectorUpdate
import com.sorsix.album_collector.domain.Collector
import com.sorsix.album_collector.domain.ERole
import com.sorsix.album_collector.domain.PrivateAlbumInstance
import com.sorsix.album_collector.domain.Role
import com.sorsix.album_collector.repository.CollectorRepository
import com.sorsix.album_collector.repository.RoleRepository
import com.sorsix.album_collector.service.CollectorService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import javax.persistence.EntityNotFoundException

@Service
class CollectorService(
    val collectorRepository: CollectorRepository,
    val roleRepository: RoleRepository,
    val encoder: PasswordEncoder,
) : CollectorService {

    override fun createCollector(collectorRegistration: CollectorRegistration): Collector {
        val collector = Collector(
            name = collectorRegistration.name,
            surname = collectorRegistration.surname,
            email = collectorRegistration.email,
            password = encoder.encode(collectorRegistration.password)
        )
        val roles: MutableSet<Role> = HashSet()
        roleRepository.save(Role(1, ERole.ROLE_USER))//delete row
        val userRole: Role = roleRepository.findByName(ERole.ROLE_USER)
            ?: throw EntityNotFoundException("ROLE_USER not found in database")
        roles.add(userRole)
        collector.roles = roles
        return collectorRepository.save(collector)
    }

    override fun setProfilePicture(collectorId: Long, file: MultipartFile) {
        val collector: Collector = collectorRepository.findByIdOrNull(collectorId)
            ?: throw EntityNotFoundException("Collector with given id does not exist")
        collector.profilePicture = file.bytes
        collectorRepository.save(collector)
    }

    override fun getProfilePicture(collectorId: Long): ByteArray {
        val collector: Collector = collectorRepository.findByIdOrNull(collectorId)
            ?: throw EntityNotFoundException("Collector with given id does not exist")
        return collector.profilePicture
    }

    override fun emailTaken(email: String): Boolean {
        return collectorRepository.existsByEmail(email)
    }

    override fun getPrivateAlbums(collectorId: Long): List<PrivateAlbumInstance> {
        val collector: Collector = collectorRepository.findByIdOrNull(collectorId)
            ?: throw EntityNotFoundException("Collector with given id does not exist")
        return collector.albums
    }

    override fun getCollector(collectorId: Long): Collector {
        return collectorRepository.findByIdOrNull(collectorId)
            ?: throw EntityNotFoundException("Collector with given id not found")
    }

    override fun updateCollector(collectorUpdate: CollectorUpdate): Collector {
        val toUpdate=collectorRepository.findByIdOrNull(collectorUpdate.id)
            ?: throw EntityNotFoundException("Collector with given id does not exist")
        toUpdate.email=collectorUpdate.email
        toUpdate.name=collectorUpdate.name
        toUpdate.surname=collectorUpdate.surname
        toUpdate.password=collectorUpdate.password
        collectorRepository.save(toUpdate)
        return toUpdate
    }


}