package com.sorsix.album_collector.api.dtos

data class CollectorUpdate(
                        val id:Long,
                        val name: String,
                       val surname: String,
                       val email: String,
                       val password: String) {
}