package team.waggly.backend.dto

import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.model.Post
import team.waggly.backend.model.User
import team.waggly.backend.repository.CommentRepository
import team.waggly.backend.repository.PostLikeRepository
import java.time.LocalDateTime

class PostDto {
    data class CreatePostRequestDto (
        val title: String,
        val description: String,
        val college: CollegeType,
        val isAnonymous: Boolean,
    ) {
        fun toEntity(user: User): Post = Post(
            title = title,
            description = description,
            college = college,
            author = user,
            isAnonymous = if (isAnonymous) 1 else 0
        )
    }

    data class CreatePostResponseDto (
        val success: Boolean
    )

    data class UpdatePostRequestDto(
        val title: String?,
        val description: String?,
        val college: CollegeType?,
    ) {
        fun updateEntity(post: Post) {
        post.title = title ?: post.title
        post.description = description ?: post.description
        post.college = college ?: post.college
        post.modifiedAt = LocalDateTime.now()
        }
    }

    data class DeletePostResponseDto (
        val success: Boolean
    )

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

    // 수정
    data class CollegePostsResponseDto(
        val bestPost: PostDetailsResponseDto,
        val posts: List<PostDetailsResponseDto>,
        )
}