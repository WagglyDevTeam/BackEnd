package team.waggly.backend.service

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.dto.myPageDto.MyPostsResponseDto
import team.waggly.backend.dto.postDto.PostSummaryResponseDto
import team.waggly.backend.model.Post
import team.waggly.backend.model.User
import team.waggly.backend.repository.*

class MyPageService (
    private val postRepository: PostRepository,
    private val postLikeRepository: PostLikeRepository,
    private val commentLikeRepository: CommentLikeRepository,
    private val postImageRepository: PostImageRepository,
    private val commentRepository: CommentRepository,
) {
    fun getAllMyPost(pageable: Pageable, user: User): List<MyPostsResponseDto> {
        val allPosts: List<Post> = postRepository.findByAuthorAndActiveStatus(user, ActiveStatusType.ACTIVE)
        val postsDto: MutableList<MyPostsResponseDto> = arrayListOf()

        if(allPosts.isNotEmpty()) {
            for (post in allPosts) {
                val dto: MyPostsResponseDto = updateMyPostsResponseDto(post, user.id!!)
                postsDto.add(dto)
            }
        }

        val start: Long = pageable.offset
        val end: Long = if ((start + pageable.pageSize) > postsDto.size) postsDto.size.toLong() else (start + pageable.pageSize)
        return PageImpl(postsDto.subList(start.toInt(), end.toInt()), pageable, postsDto.size.toLong()).toList()

    }

    private fun updateMyPostsResponseDto(post: Post, userId: Long): MyPostsResponseDto {
        val myPostsResponseDto = MyPostsResponseDto(post)

        myPostsResponseDto.postImageCnt = postImageRepository.countByPostId(post.id!!)
        myPostsResponseDto.postLikeCnt = postLikeRepository.countByPostIdAndStatus(post.id, ActiveStatusType.ACTIVE)
        myPostsResponseDto.postCommentCnt = commentRepository.countByPostId(post.id)
        myPostsResponseDto.isLikedByMe = postLikeRepository.existsByIdAndUserIdAndStatus(post.id, userId, ActiveStatusType.ACTIVE)

        return myPostsResponseDto
    }
}