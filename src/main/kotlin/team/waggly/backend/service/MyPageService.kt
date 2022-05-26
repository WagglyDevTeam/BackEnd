package team.waggly.backend.service

import team.waggly.backend.repository.CommentLikeRepository
import team.waggly.backend.repository.PostLikeRepository
import team.waggly.backend.repository.PostRepository

class MyPageService (
    private val postRepository: PostRepository,
    private val postLikeRepository: PostLikeRepository,
    private val commentLikeRepository: CommentLikeRepository,
) {

}