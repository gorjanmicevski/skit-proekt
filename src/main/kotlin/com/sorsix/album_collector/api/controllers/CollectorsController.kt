package com.sorsix.album_collector.api.controllers

import com.sorsix.album_collector.api.dtos.CollectorRegistration
import com.sorsix.album_collector.api.dtos.CollectorUpdate
import com.sorsix.album_collector.api.dtos.JwtResponse
import com.sorsix.album_collector.api.dtos.LoginRequest
import com.sorsix.album_collector.domain.Collector
import com.sorsix.album_collector.domain.PrivateAlbumInstance
import com.sorsix.album_collector.security.jwt.JwtUtils
import com.sorsix.album_collector.security.service.UserDetailsImpl
import com.sorsix.album_collector.service.CollectorService
import com.sorsix.album_collector.service.PostService
import io.jsonwebtoken.Jwts
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import kotlin.streams.toList

@RestController
@RequestMapping("/api/collectors")
@CrossOrigin(origins = ["http://localhost:4200"])
class CollectorsController(
    val postService: PostService,
    val collectorService: CollectorService,
    val authenticationManager: AuthenticationManager,
    val jwtUtils: JwtUtils
) {
    // Collectors

    @PostMapping("/registerCollector")
    fun registerCollector(@RequestBody collectorRegistration: CollectorRegistration): ResponseEntity<Any> {
        if (collectorService.emailTaken(collectorRegistration.email)) {
            return ResponseEntity.badRequest().body("Error: Email already used")
        }
        return ResponseEntity.ok(collectorService.createCollector(collectorRegistration))
    }
    @PostMapping("/updateCollector")
    fun updateCollector(@RequestBody collectorUpdate: CollectorUpdate):ResponseEntity<Any>{
        if(collectorService.getCollector(collectorUpdate.id).email == collectorUpdate.email){
            return ResponseEntity.ok(collectorService.updateCollector(collectorUpdate))
        }
        if(collectorService.emailTaken(collectorUpdate.email)){
            return ResponseEntity.badRequest().body("Error: Email already used")
        }
        return ResponseEntity.ok(collectorService.updateCollector(collectorUpdate))
    }
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt: String = jwtUtils.generateJwtToken(authentication)

        val userDetails: UserDetailsImpl = authentication.principal as UserDetailsImpl
        val roles: List<String> = userDetails.authorities.stream().map { it.authority }.toList()
        return ResponseEntity.ok(
            JwtResponse(
                token = jwt,
                id = userDetails.id,
                name = userDetails.name,
                surname = userDetails.surname,
                email = userDetails.email,
                roles = roles,
                albums = userDetails.albums,
                profilePicture = userDetails.profilePicture,
                expiration = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body.expiration
            )
        )
    }

    @PostMapping("/{collectorId}/setProfilePicture")
    fun setProfilePicture(@PathVariable collectorId: Long, @RequestParam file: MultipartFile): ResponseEntity<Any> {
        collectorService.setProfilePicture(collectorId, file)
        return ResponseEntity.created(URI("/setProfilePicture/${collectorId}")).build()
    }

    @GetMapping("/{collectorId}/getProfilePicture")
    fun getProfilePicture(@PathVariable collectorId: Long): ResponseEntity<Any> {
        val image: ByteArray = collectorService.getProfilePicture(collectorId)
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${System.currentTimeMillis()}\"")
            .body(image)
    }

    @GetMapping("/{collectorId}/privateAlbums")
    fun getPrivateAlbums(
        @PathVariable collectorId: Long
    ): ResponseEntity<List<PrivateAlbumInstance>> {
        return ResponseEntity.ok(collectorService.getPrivateAlbums(collectorId))
    }

    @GetMapping("/{collectorId}")
    fun getCollector(
        @PathVariable collectorId: Long
    ): ResponseEntity<Collector> {
        return ResponseEntity.ok(collectorService.getCollector(collectorId))
    }
}