package team.waggly.backend.dto.postDto

import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.model.Post
import team.waggly.backend.repository.CommentRepository
import team.waggly.backend.repository.PostLikeRepository
import java.time.LocalDateTime

// 수정
data class PostDetailsResponseDto(
    var postId: Long?,
    var postTitle: String,
    var postDesc: String,
    var postCreatedAt: LocalDateTime,
//        var authorMajor: String,
//        var postImageCnt: Int,
    var postLikeCnt: Int,
    var postCommentCnt: Int,
    var isLikedByMe: Boolean,
    var isBlind: Boolean,
    var isAnonymous: Boolean,
) {
    constructor(post: Post,
                postLikeRepository: PostLikeRepository,
                commentRepository: CommentRepository,
                userId: Long?,
    ): this(
        post.id,
        post.title,
        post.description,
        post.createdAt,
        postLikeRepository.countByPostId(post.id!!),
        commentRepository.countByPostId(post.id!!),
        if (userId != null) postLikeRepository.existsByUserId(userId!!) else false,
        post.activeStatus == ActiveStatusType.INACTIVE,
        post.isAnonymous != 0,
    )
}