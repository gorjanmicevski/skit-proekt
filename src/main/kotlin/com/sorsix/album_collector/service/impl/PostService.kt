package com.sorsix.album_collector.service.impl

import com.sorsix.album_collector.api.dtos.PostCreator
import com.sorsix.album_collector.domain.Album
import com.sorsix.album_collector.domain.Collector
import com.sorsix.album_collector.domain.Post
import com.sorsix.album_collector.repository.AlbumRepository
import com.sorsix.album_collector.repository.CollectorRepository
import com.sorsix.album_collector.repository.PostRepository
import com.sorsix.album_collector.service.PostService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException

@Service
class PostService(
    val postRepository: PostRepository,
    val collectorRepository: CollectorRepository,
    val albumRepository: AlbumRepository
) : PostService {

    override fun getAllPaginated(page: Int, pageSize: Int, albumId: Long?): List<Post> {
        return if (albumId != null) postRepository.findByAlbum_Id(
            albumId,
            PageRequest.of(page, pageSize, Sort.by("dateTimeCreated").descending())
        ).content
        else postRepository.findAll(
            PageRequest.of(
                page, pageSize, Sort.by("dateTimeCreated").descending()
            )
        ).content

    }

    override fun create(post: PostCreator, imageMissing: MultipartFile?, imageDuplicates: MultipartFile?): Post {
        val collector: Collector = collectorRepository.findByIdOrNull(post.collectorId)
            ?: throw EntityNotFoundException("Collector with given id does not exist")
        val album: Album = albumRepository.findByIdOrNull(post.albumId)
            ?: throw EntityNotFoundException("Album with given id does not exist")
        return postRepository.save(
            Post(
                collector = collector,
                collectorName = collector.name,
                description = post.description,
                phone = post.phone,
                location = post.location,
                album = album,
                duplicateStickers = post.duplicateStickers,
                missingStickers = post.missingStickers,
                imageMissingStickers = imageMissing?.bytes,
                imageDuplicatesStickers = imageDuplicates?.bytes,
                dateTimeCreated = LocalDateTime.now()
            )
        )
    }

    override fun update(
        post: PostCreator, imageMissing: MultipartFile?, imageDuplicates: MultipartFile?, postId: Long
    ): Post {
        val postToUpdate: Post =
            postRepository.findByIdOrNull(postId) ?: throw EntityNotFoundException("Post with given id does not exist")
        val collector: Collector = collectorRepository.findByIdOrNull(post.collectorId)
            ?: throw EntityNotFoundException("Collector with given id does not exist")
        val album: Album = albumRepository.findByIdOrNull(post.albumId)
            ?: throw EntityNotFoundException("Album with given id does not exist")
        postToUpdate.description = post.description
        postToUpdate.phone = post.phone
        postToUpdate.location = post.location
        postToUpdate.album = album
        postToUpdate.duplicateStickers = post.duplicateStickers
        postToUpdate.missingStickers = post.missingStickers
        postToUpdate.imageMissingStickers = imageMissing?.bytes
        postToUpdate.imageDuplicatesStickers = imageDuplicates?.bytes
        postToUpdate.collectorName = collector.name
        return postRepository.save(postToUpdate)
    }
}