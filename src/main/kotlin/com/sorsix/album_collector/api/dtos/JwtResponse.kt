package com.sorsix.album_collector.api.dtos

import com.sorsix.album_collector.domain.PrivateAlbumInstance
import java.util.*

data class JwtResponse(
    val token: String,
    val type: String = "Bearer",
    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    val roles: List<String>,
    val albums: List<PrivateAlbumInstance>,
    val profilePicture: ByteArray,
    val expiration:Date
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JwtResponse

        if (!profilePicture.contentEquals(other.profilePicture)) return false

        return true
    }

    override fun hashCode(): Int {
        return profilePicture.contentHashCode()
    }
}
