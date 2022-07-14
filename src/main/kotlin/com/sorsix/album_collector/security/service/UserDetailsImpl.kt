package com.sorsix.album_collector.security.service

import com.fasterxml.jackson.annotation.JsonIgnore
import com.sorsix.album_collector.domain.Collector
import com.sorsix.album_collector.domain.PrivateAlbumInstance
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import kotlin.streams.toList

class UserDetailsImpl(
    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    @JsonIgnore
    private val password: String,
    //mozhe da treba mutable
    private val authorities: Collection<GrantedAuthority>,
    val albums: List<PrivateAlbumInstance>,
    val profilePicture: ByteArray,
) : UserDetails {
    val serialVersionUID = 1L

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null || this::class != other::class)
            return false
        val user: UserDetailsImpl = other as UserDetailsImpl
        return Objects.equals(id, user.id)
    }

    companion object {
        fun build(collector: Collector): UserDetailsImpl {
            //mozhe da treba mutable
            val authority: List<GrantedAuthority> = collector.roles.stream()
                .map { SimpleGrantedAuthority(it.name.name) }
                .toList()//mozhe da treba mutable
            return UserDetailsImpl(
                id = collector.id,
                name = collector.name,
                surname = collector.surname,
                email = collector.email,
                password = collector.password,
                albums = collector.albums,
                profilePicture = collector.profilePicture,
                authorities = authority
            )
        }
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

}