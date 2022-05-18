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
    var postImageCnt: Int?,
    var postLikeCnt: Int?,
    var postCommentCnt: Int?,
    var isLikedByMe: Boolean?,
    var isBlind: Boolean,
    var isAnonymous: Boolean,
) {
    constructor(post: Post): this(
        post.id,
        post.title,
        post.description,
        post.createdAt,
            null,
        null,
        null,
        null,
        post.activeStatus == ActiveStatusType.INACTIVE,
        post.isAnonymous != 0,
    )
}