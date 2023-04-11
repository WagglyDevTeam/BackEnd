package team.waggly.backend.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.dto.home.HomeResponseDto
import team.waggly.backend.dto.post.PostDto
import team.waggly.backend.model.Post
import team.waggly.backend.model.User
import team.waggly.backend.repository.*
import java.util.*

@Service
class HomeService(
    private val postRepository: PostRepository,
    private val postLikeRepository: PostLikeRepository,
    private val postImageRepository: PostImageRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
) {
    fun getHome(userId: Long?): HomeResponseDto {
        val user = userRepository.findByIdOrNull(userId)
        val colleges = CollegeType.values()
        val userCollege = user?.major?.college ?: colleges[Random().nextInt(colleges.size)]
        val randomCollege = colleges[Random().nextInt(colleges.size)]

        // 해당 단과대의 베스트 게시글 ID -> 게시글 찾아옴 -> summary dto 생성
        val collegeBestId: Long = postLikeRepository.getMostLikedPostInCollege(userCollege.name)
            ?: 1
//            postRepository.findFirstByCollegeAndActiveStatusOrderByIdDesc(userCollege, ActiveStatusType.ACTIVE).id
//            ?: throw IllegalArgumentException("해당 학과에 게시글이 없습니다.")

        val bestPost: Post =
            postRepository.findByIdOrNull(collegeBestId) ?: throw IllegalArgumentException("해당 게시글이 없습니다.")

        val bestPostDto: PostDto = this.updatePostDto(bestPost, userId)

        val bestPostWithCollegeType = Pair(userCollege, bestPostDto)

        val randomCollegeBestId: Long = postLikeRepository.getMostLikedPostInCollege(randomCollege.name)
            ?: 1
//            postRepository.findFirstByCollegeAndActiveStatusOrderByIdDesc(randomCollege, ActiveStatusType.ACTIVE).id
//            ?: throw IllegalArgumentException("해당 학과에 게시글이 없습니다.")

        val randomBestPost: Post =
            postRepository.findByIdOrNull(randomCollegeBestId) ?: throw IllegalArgumentException("해당 게시글이 없습니다.")

        val randomBestPostDto: PostDto = this.updatePostDto(randomBestPost, userId)

        return HomeResponseDto(bestPostWithCollegeType, randomBestPostDto)
    }


    private fun updatePostDto(post: Post, userId: Long?): PostDto {
        val postDto = PostDto(post)

        postDto.postImageCnt = postImageRepository.countByPostId(post.id!!)
        postDto.postLikeCnt = postLikeRepository.countByPostIdAndStatus(post.id, ActiveStatusType.ACTIVE)
        postDto.postCommentCnt = commentRepository.countByPostIdAndActiveStatus(post.id, ActiveStatusType.ACTIVE)
        postDto.isLikedByMe =
            if (userId != null) postLikeRepository.existsByPostIdAndUserIdAndStatus(
                post.id,
                userId,
                ActiveStatusType.ACTIVE
            )
            else false

        return postDto
    }
}