package com.sorsix.album_collector.api.dtos

import com.sorsix.album_collector.domain.Sticker

data class PrivateAlbumStickers(
    val allStickers: List<Sticker>, val collectedStickers: List<Sticker>, val duplicateStickers: List<Sticker>
)
