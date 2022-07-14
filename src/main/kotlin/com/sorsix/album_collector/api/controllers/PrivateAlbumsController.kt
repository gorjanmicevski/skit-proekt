package com.sorsix.album_collector.api.controllers

import com.sorsix.album_collector.api.dtos.PrivateAlbumStickers
import com.sorsix.album_collector.api.dtos.StickerNumberList
import com.sorsix.album_collector.domain.PrivateAlbumInstance
import com.sorsix.album_collector.service.PrivateAlbumInstanceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/privateAlbum")
@CrossOrigin(origins = ["http://localhost:4200"])
class PrivateAlbumsController(
    val privateAlbumInstanceService: PrivateAlbumInstanceService
) {
    //PrivateAlbums

    @PostMapping("/create")
    fun addAlbumForCollector(
        @RequestParam collectorId: Long, @RequestParam albumId: Long
    ): ResponseEntity<PrivateAlbumInstance> {
        return ResponseEntity.ok(privateAlbumInstanceService.createPrivateInstance(collectorId, albumId))
    }

    @GetMapping("/missingStickers")
    fun getMissingStickers(
        @RequestParam collectorId: Long, @RequestParam albumId: Long
    ): ResponseEntity<String> {
        return ResponseEntity.ok(privateAlbumInstanceService.getMissingStickers(collectorId, albumId))
    }
    @GetMapping("/duplicateStickers")
    fun getDuplicateStickers(
        @RequestParam collectorId: Long, @RequestParam albumId: Long
    ): ResponseEntity<String> {
        return ResponseEntity.ok(privateAlbumInstanceService.getDuplicateStickers(collectorId, albumId))
    }

    @PutMapping("/{paId}/collectStickers")
    fun collectSticker(
        @PathVariable paId: Long,
        @RequestBody stickerNumbers: StickerNumberList
    ) {
        privateAlbumInstanceService.addNewCollectedSticker(paId, stickerNumbers.stickerNumbers)
    }

    @PutMapping("/{paId}/duplicateStickers")
    fun duplicateStickers(
        @PathVariable paId: Long,
        @RequestBody stickerNumbers: StickerNumberList
    ) {
        privateAlbumInstanceService.addDuplicateStickers(paId,stickerNumbers.stickerNumbers)
    }

    @PutMapping("/{paId}/missingStickers")
    fun addMissingStickers(
        @PathVariable paId: Long,
        @RequestBody stickerNumbers: StickerNumberList
    ){
        privateAlbumInstanceService.setMissingStickers(paId,stickerNumbers.stickerNumbers)
    }
//    @PutMapping("/{paId}/removeSticker")
//    fun removeSticker(
//        @PathVariable paId: Long, @RequestParam stickerNumber: String
//    ) {
//        privateAlbumInstanceService.removeCollectedSticker(paId, stickerNumber)
//    }
//
//    @PutMapping("/{paId}/removeDuplicate")
//    fun removeDuplicateSticker(
//        @PathVariable paId: Long, @RequestParam stickerNumber: String
//    ) {
//        privateAlbumInstanceService.removeDuplicateSticker(paId, stickerNumber)
//    }

    @GetMapping("/{paId}/stickers")
    fun getPrivateAlbumStickers(
        @PathVariable paId: Long
    ): ResponseEntity<PrivateAlbumStickers> {
        return ResponseEntity.ok(privateAlbumInstanceService.getAllStickers(paId))
    }
}