package com.sorsix.album_collector.service

import com.sorsix.album_collector.api.dtos.PostCreator
import com.sorsix.album_collector.domain.Post
import org.springframework.web.multipart.MultipartFile

interface PostService {
    fun getAllPaginated(page: Int, pageSize: Int, albumId: Long?): List<Post>
    fun create(post: PostCreator, imageMissing: MultipartFile?, imageDuplicates: MultipartFile?): Post
    fun update(post: PostCreator, imageMissing: MultipartFile?, imageDuplicates: MultipartFile?, postId: Long): Post
}