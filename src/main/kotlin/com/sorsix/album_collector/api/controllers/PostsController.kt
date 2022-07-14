package com.sorsix.album_collector.api.controllers

import com.sorsix.album_collector.api.dtos.PostCreator
import com.sorsix.album_collector.domain.Post
import com.sorsix.album_collector.service.PostService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = ["http://localhost:4200"])
class PostsController(
    val postService: PostService
) {
    //Posts

    @GetMapping()
    fun getPostsPaginated(
        @RequestParam page: Int, @RequestParam pageSize: Int, @RequestParam albumId: Long?
    ): ResponseEntity<List<Post>> {
        return ResponseEntity.ok(postService.getAllPaginated(page, pageSize, albumId))
    }

    @PostMapping("/create")
    fun createPost(
        @RequestBody postCreator: PostCreator,
        @RequestParam(required = false) imageMissing: MultipartFile?,
        @RequestParam(required = false) imageDuplicates: MultipartFile?
    ): ResponseEntity<Post> {
        println(postCreator)
        return ResponseEntity.ok(
            postService.create(
                post = postCreator, imageDuplicates = imageDuplicates, imageMissing = imageMissing
            )
        )
    }

    @PutMapping("/{postId}/update")
    fun updatePost(
        @PathVariable postId: Long,
        @RequestBody postCreator: PostCreator,
        @RequestParam(required = false) imageMissing: MultipartFile?,
        @RequestParam(required = false) imageDuplicates: MultipartFile?
    ): ResponseEntity<Post> {
        return ResponseEntity.ok(
            postService.update(
                post = postCreator, imageMissing = imageMissing, imageDuplicates = imageDuplicates, postId = postId
            )
        )
    }
}