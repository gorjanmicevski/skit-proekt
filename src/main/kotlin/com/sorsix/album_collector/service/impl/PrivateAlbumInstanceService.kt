package com.sorsix.album_collector.service.impl

import com.sorsix.album_collector.api.dtos.PrivateAlbumStickers
import com.sorsix.album_collector.domain.Album
import com.sorsix.album_collector.domain.Collector
import com.sorsix.album_collector.domain.PrivateAlbumInstance
import com.sorsix.album_collector.repository.AlbumRepository
import com.sorsix.album_collector.repository.CollectorRepository
import com.sorsix.album_collector.repository.PrivateAlbumInstanceRepository
import com.sorsix.album_collector.repository.StickerRepository
import com.sorsix.album_collector.service.PrivateAlbumInstanceService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class PrivateAlbumInstanceService(
    val privateAlbumRepository: PrivateAlbumInstanceRepository,
    val collectorRepository: CollectorRepository,
    val albumRepository: AlbumRepository,
    val stickerRepository: StickerRepository
) : PrivateAlbumInstanceService {
    override fun createPrivateInstance(collectorId: Long, albumId: Long): PrivateAlbumInstance {
        val collector: Collector = collectorRepository.findByIdOrNull(collectorId)
            ?: throw EntityNotFoundException("Collector with given id does not exist")
        val album: Album = albumRepository.findByIdOrNull(albumId)
            ?: throw EntityNotFoundException("Album with given id does not exist")
        if (privateAlbumRepository.findByCollectorAndAlbum(
                collector, album
            ) != null
        ) throw IllegalArgumentException("Entity already exists")
        val privateAlbum = PrivateAlbumInstance(
            collector = collector,
            album = album,
            collectedStickers = mutableListOf(),
            duplicateStickers = mutableListOf()
        )
        return privateAlbumRepository.save(privateAlbum)
    }

    override fun getMissingStickers(collectorId: Long, albumId: Long): String {
        val collector: Collector = collectorRepository.findByIdOrNull(collectorId)
            ?: throw EntityNotFoundException("Collector with given id is not found")
        val album: Album =
            albumRepository.findByIdOrNull(albumId) ?: throw EntityNotFoundException("Album with given id is not found")
        val privateAlbum: PrivateAlbumInstance = privateAlbumRepository.findByCollectorAndAlbum(collector, album)
            ?: throw EntityNotFoundException("Private album for given collector does not exist")
        val stickers = album.stickers.filter {
            !privateAlbum.collectedStickers.contains(it) && !privateAlbum.duplicateStickers.contains(it)
        }.joinToString { it.number }
        println(stickers)
        return stickers
    }

    override fun getDuplicateStickers(collectorId: Long, albumId: Long): String {
        val collector: Collector = collectorRepository.findByIdOrNull(collectorId)
            ?: throw EntityNotFoundException("Collector with given id is not found")
        val album: Album =
            albumRepository.findByIdOrNull(albumId) ?: throw EntityNotFoundException("Album with given id is not found")
        val privateAlbum: PrivateAlbumInstance = privateAlbumRepository.findByCollectorAndAlbum(collector, album)
            ?: throw EntityNotFoundException("Private album for given collector does not exist")
        val stickers =
            privateAlbum.duplicateStickers  .joinToString { it.number }
        println(stickers)
        return stickers
    }

    override fun getAllStickers(paId: Long): PrivateAlbumStickers {

        val privateAlbum: PrivateAlbumInstance = privateAlbumRepository.findByIdOrNull(paId)
            ?: throw EntityNotFoundException("Private album with given id does not exist")
        val album: Album = albumRepository.findByIdOrNull(privateAlbum.album.id)
            ?: throw EntityNotFoundException("Album with given id is not found")

        return PrivateAlbumStickers(
            allStickers = album.stickers,
            collectedStickers = privateAlbum.collectedStickers,
            duplicateStickers = privateAlbum.duplicateStickers
        )
    }

    override fun addNewCollectedSticker(paId: Long, stickerNumbers: List<String>) {

        val privateAlbum: PrivateAlbumInstance = privateAlbumRepository.findByIdOrNull(paId)
            ?: throw EntityNotFoundException("Private album with given id does not exist")
        val album = privateAlbum.album
        for (stickerNumber in stickerNumbers) {
            val sticker = stickerRepository.findByNumberAndAlbum(stickerNumber, album) ?: continue
//                ?: throw EntityNotFoundException("Sticker with given number does not exist in given album")
//            if (privateAlbum.collectedStickers.contains(sticker)) privateAlbum.duplicateStickers.add(sticker)
            if (!privateAlbum.collectedStickers.contains(sticker)) {
                privateAlbum.collectedStickers.add(sticker)
                privateAlbum.duplicateStickers.removeIf { it.number == sticker.number }
            }
        }
        privateAlbumRepository.save(privateAlbum)
    }

    override fun addDuplicateStickers(paId: Long, stickerNumbers: List<String>) {
        val privateAlbum: PrivateAlbumInstance = privateAlbumRepository.findByIdOrNull(paId)
            ?: throw EntityNotFoundException("Private album with given id does not exist")
        val album = privateAlbum.album
        for (stickerNumber in stickerNumbers) {
            val sticker = stickerRepository.findByNumberAndAlbum(stickerNumber, album) ?: continue
//                ?: throw EntityNotFoundException("Sticker with given number does not exist in given album")
            if (!privateAlbum.duplicateStickers.contains(sticker)) {
                privateAlbum.duplicateStickers.add(sticker)
                privateAlbum.collectedStickers.removeIf { it.number == sticker.number }
            }
        }
        privateAlbumRepository.save(privateAlbum)
    }

//    override fun removeCollectedSticker(paId: Long, stickerNumber: String) {
//
//        val privateAlbum: PrivateAlbumInstance = privateAlbumRepository.findByIdOrNull(paId)
//            ?: throw EntityNotFoundException("Private album with given id does not exist")
//
//        privateAlbum.collectedStickers.removeIf { it.number == stickerNumber }
//        privateAlbum.duplicateStickers.removeIf { it.number == stickerNumber }
//        privateAlbumRepository.save(privateAlbum)
//    }

//    override fun removeDuplicateSticker(paId: Long, stickerNumber: String) {
//
//        val privateAlbum: PrivateAlbumInstance = privateAlbumRepository.findByIdOrNull(paId)
//            ?: throw EntityNotFoundException("Private album with given id does not exist")
//
//        privateAlbum.duplicateStickers.removeIf { it.number == stickerNumber }
//        privateAlbumRepository.save(privateAlbum)
//    }

    override fun setMissingStickers(paId: Long, stickerNumbers: List<String>) {
        val privateAlbum: PrivateAlbumInstance = privateAlbumRepository.findByIdOrNull(paId)
            ?: throw EntityNotFoundException("Private album with given id does not exist")
        for (stickerNumber in stickerNumbers) {
            privateAlbum.duplicateStickers.removeIf { it.number == stickerNumber }
            privateAlbum.collectedStickers.removeIf { it.number == stickerNumber }
        }
        privateAlbumRepository.save(privateAlbum)
    }
}