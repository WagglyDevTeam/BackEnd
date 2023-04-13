package team.waggly.backend.service

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.dto.myPageDto.MyCommentsDetailDto
import team.waggly.backend.dto.myPageDto.MyCommentsResponseDto
import team.waggly.backend.dto.myPageDto.MyPostsDetailDto
import team.waggly.backend.dto.myPageDto.MyPostsResponseDto
import team.waggly.backend.model.Comment
import team.waggly.backend.model.Post
import team.waggly.backend.model.User
import team.waggly.backend.repository.*

@Service
class MyPageService (
    private val postRepository: PostRepository,
    private val postLikeRepository: PostLikeRepository,
    private val postImageRepository: PostImageRepository,
    private val commentRepository: CommentRepository,
) {
    // 내가 쓴 게시글 조회
    fun getAllMyPosts(pageable: Pageable, user: User): MyPostsResponseDto {
        val allPosts: List<Post> =
            postRepository.findByAuthorAndActiveStatusOrderByCreatedAtDesc(user, ActiveStatusType.ACTIVE)
        val postsDto: MutableList<MyPostsDetailDto> = arrayListOf()

        if (allPosts.isNotEmpty()) {
            for (post in allPosts) {
                val myPostsDetailDto = MyPostsDetailDto(post)

                myPostsDetailDto.postImageCnt = postImageRepository.countByPostId(post.id!!)
                myPostsDetailDto.postLikeCnt = postLikeRepository.countByPostIdAndStatus(post.id, ActiveStatusType.ACTIVE)
                myPostsDetailDto.postCommentCnt = commentRepository.countByPostIdAndActiveStatus(post.id, ActiveStatusType.ACTIVE)
                myPostsDetailDto.isLikedByMe = postLikeRepository.existsByPostIdAndUserIdAndStatus(post.id, user.id!!, ActiveStatusType.ACTIVE)

                postsDto.add(myPostsDetailDto)
            }
        }

        val start: Long = pageable.offset
        val end: Long =
            if ((start + pageable.pageSize) > postsDto.size) postsDto.size.toLong() else (start + pageable.pageSize)
        return MyPostsResponseDto(PageImpl(postsDto.subList(start.toInt(), end.toInt()), pageable, postsDto.size.toLong()).toList())
    }

    // 내가 쓴 댓글 조회
    fun getAllMyComments(pageable: Pageable, user: User): MyCommentsResponseDto {
        val allComments: List<Comment> = commentRepository.findByUserAndActiveStatusOrderByCreatedAtDesc(user, ActiveStatusType.ACTIVE)
        val commentsDto: MutableList<MyCommentsDetailDto> = arrayListOf()

        if (allComments.isNotEmpty()) {
            for (comment in allComments) {
                commentsDto.add(MyCommentsDetailDto(comment))
            }
        }

        val start: Long = pageable.offset
        val end: Long =
            if ((start + pageable.pageSize) > commentsDto.size) commentsDto.size.toLong() else (start + pageable.pageSize)
        return MyCommentsResponseDto(PageImpl(commentsDto.subList(start.toInt(), end.toInt()), pageable, commentsDto.size.toLong()).toList())
    }
}