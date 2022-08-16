package team.waggly.backend.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.home.HomeResponseDto
import team.waggly.backend.dto.postDto.CollegePostsResponseDto
import team.waggly.backend.dto.postDto.PostSummaryResponseDto
import team.waggly.backend.model.Post
import team.waggly.backend.model.User
import team.waggly.backend.repository.*
import java.util.*

@Service
class HomeService(
    private val postRepository: PostRepository,
    private val postLikeRepository: PostLikeRepository,
    private val postImageRepository: PostImageRepository,
    private val commentRepository: CommentRepository
) {
    fun getHome(user: User?): HomeResponseDto {
        val colleges = CollegeType.values()
        val userId: Long? = user?.id
        val userCollege = user?.major?.college ?: colleges[Random().nextInt(colleges.size)]
        val randomCollege = colleges[Random().nextInt(colleges.size)]

        // 해당 단과대의 베스트 게시글 ID -> 게시글 찾아옴 -> summary dto 생성
        val collegeBestId: Long = postLikeRepository.getMostLikedPostInCollege(userCollege.toString()) ?: 0
        val bestPost: Post = postRepository.findByIdOrNull(collegeBestId) ?: throw IllegalArgumentException("해당 게시글이 없습니다.")
        val bestPostSummary: PostSummaryResponseDto = this.updatePostSummaryResponseDto(bestPost, userId)

        val bestPostWithCollegeType = Pair(userCollege, bestPostSummary)

        val randomCollegeBestId: Long = postLikeRepository.getMostLikedPostInCollege(randomCollege.toString()) ?: 0
        val randomBestPost: Post = postRepository.findByIdOrNull(randomCollegeBestId) ?: throw IllegalArgumentException("해당 게시글이 없습니다.")
        val randomBestPostSummary: PostSummaryResponseDto = this.updatePostSummaryResponseDto(randomBestPost, userId)

        return HomeResponseDto(bestPostWithCollegeType,randomBestPostSummary)
    }


    private fun updatePostSummaryResponseDto(post: Post, userId: Long?): PostSummaryResponseDto {
        val postSummaryResponseDto = PostSummaryResponseDto(post)

        postSummaryResponseDto.postImageCnt = postImageRepository.countByPostId(post.id!!)
        postSummaryResponseDto.postLikeCnt = postLikeRepository.countByPostIdAndStatus(post.id, ActiveStatusType.ACTIVE)
        postSummaryResponseDto.postCommentCnt = commentRepository.countByPostId(post.id)
        postSummaryResponseDto.isLikedByMe =
            if(userId != null) postLikeRepository.existsByIdAndUserIdAndStatus(post.id, userId, ActiveStatusType.ACTIVE)
            else false

        return postSummaryResponseDto
    }
}