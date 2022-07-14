package com.sorsix.album_collector.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIncludeProperties
import javax.persistence.*

@Entity
data class Collector(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var name: String,
    var surname: String,
    @Column(unique = true)
    var email: String,
    @JsonIgnore
    var password: String,
    @ManyToMany(fetch = FetchType.LAZY)
    //da se sredi ova da ne bide var 03:06 e nemom poke nemom
    var roles: MutableSet<Role> = HashSet(),
    @OneToMany(mappedBy = "collector")
    @JsonIncludeProperties("album")
    val albums: MutableList<PrivateAlbumInstance> = mutableListOf(),
    @JsonIgnore
    @Lob
    var profilePicture: ByteArray = ByteArray(0)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Collector

        if (!profilePicture.contentEquals(other.profilePicture)) return false

        return true
    }

    override fun hashCode(): Int {
        return profilePicture.contentHashCode()
    }
}