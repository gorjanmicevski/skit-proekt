package com.sorsix.album_collector.api.dtos

import com.sorsix.album_collector.domain.Sticker

data class AlbumResponse(
    val Id: Long = 0,
    val name: String,
    val imageUrl: String?,
    val stickers: MutableList<Sticker> = mutableListOf()
)