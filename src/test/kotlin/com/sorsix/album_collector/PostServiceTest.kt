package com.sorsix.album_collector

import com.sorsix.album_collector.api.dtos.PostCreator
import com.sorsix.album_collector.domain.*
import com.sorsix.album_collector.repository.AlbumRepository
import com.sorsix.album_collector.repository.CollectorRepository
import com.sorsix.album_collector.repository.PostRepository
import com.sorsix.album_collector.service.impl.PostService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

class PostServiceTest {
    val postRepository: PostRepository = mockk()
    val albumRepository: AlbumRepository = mockk()
    val collectorRepository: CollectorRepository = mockk()

    val postService = PostService(postRepository, collectorRepository, albumRepository)
    val album = Album(1, "Album", ByteArray(0), mutableListOf())
    val collector = Collector(0, "Name", "Surname", "email@email.com", "pass", hashSetOf(Role(1, ERole.ROLE_USER)))
    val post = Post(
        0, collector, collector.name, "descrtiption", "070 xxx xxx", "location", album, null, null, null, null,
        LocalDateTime.now()
    )


    @Test
    fun whenCreatePost_thenReturnCreatedPost() {
        //given
        every { postRepository.save(post) } returns post
        every { collectorRepository.findByIdOrNull(0) } returns collector
        every { albumRepository.findByIdOrNull(1)} returns album
        //when
        val result = postService
            .create(
                PostCreator(
                    post.collector.id,
                    post.description,
                    post.phone,
                    post.location,
                    post.album.id,
                    null,
                    null,
                    null,
                    null
                ), null, null
            )

        //then
        Assertions.assertEquals(post, result)
        verifyOrder {
            collectorRepository.findByIdOrNull(0)
            albumRepository.findByIdOrNull(1)
            postRepository.save(post)
        }
    }

    @Test
    fun whenUpdatePost_thenReturnUpdatedPost() {
        val updatedPost = Post(
            0, collector, collector.name, "updated", "updated", "updated", album, null, null, null, null,
            LocalDateTime.now()
        )
        //given
        every { postRepository.findByIdOrNull(0) } returns post
        every { collectorRepository.findByIdOrNull(0) } returns collector
        every { albumRepository.findByIdOrNull(1) } returns album
        every { postRepository.save(updatedPost) } returns updatedPost
        //when
        val result = postService.update(
            PostCreator(0, "updated", "updated", "updated", 1, null, null, null, null),
            null,
            null,
            0
        )

        //then
        Assertions.assertEquals(updatedPost, result)
        verifyOrder {
            postRepository.findByIdOrNull(0)
            collectorRepository.findByIdOrNull(0)
            albumRepository.findByIdOrNull(1)
            postRepository.save(updatedPost)
        }
    }

}