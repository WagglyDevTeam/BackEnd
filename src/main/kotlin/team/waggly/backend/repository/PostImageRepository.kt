package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.model.Post
import team.waggly.backend.model.PostImage

@Repository
interface PostImageRepository : JpaRepository<PostImage, Long> {
    fun findAllByPostId(postId: Long): List<PostImage>
    fun findByImageUrlByDeletedAtNull(imageUrl: String): PostImage?
    fun findByPostIdByDeletedAtNull(postId: Long): PostImage?
    fun findAllByPostIdByDeletedAtNull(postId: Long): List<PostImage>?
}