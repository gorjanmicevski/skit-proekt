package com.sorsix.album_collector.api.dtos

data class CollectorRegistration(
    val name: String,
    val surname: String,
    val email: String,
    val password: String
)
