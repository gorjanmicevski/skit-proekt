package com.sorsix.album_collector.api.dtos

data class PostCreator(
    val collectorId: Long,
    val description: String,
    val phone: String,
    val location: String,
    val albumId: Long,
    val duplicateStickers: String?,
    val missingStickers: String?,
    val imageDuplicatesStickers: ByteArray?,
    val imageMissingStickers: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PostCreator

        if (imageDuplicatesStickers != null) {
            if (other.imageDuplicatesStickers == null) return false
            if (!imageDuplicatesStickers.contentEquals(other.imageDuplicatesStickers)) return false
        } else if (other.imageDuplicatesStickers != null) return false
        if (imageMissingStickers != null) {
            if (other.imageMissingStickers == null) return false
            if (!imageMissingStickers.contentEquals(other.imageMissingStickers)) return false
        } else if (other.imageMissingStickers != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = imageDuplicatesStickers?.contentHashCode() ?: 0
        result = 31 * result + (imageMissingStickers?.contentHashCode() ?: 0)
        return result
    }
}
