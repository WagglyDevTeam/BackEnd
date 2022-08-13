package team.waggly.backend.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
import team.waggly.backend.dto.postDto.PostsInHomeResponseDto
import team.waggly.backend.model.Post
import team.waggly.backend.model.User

@Repository
interface PostRepository: JpaRepository<Post, Long> {
    // PostController.getAllPosts - 게시글 전체 리스트 (Status: "ACTIVE")
    fun findAllByActiveStatusOrderByIdDesc(activeStatus: ActiveStatusType): List<Post>

    // PostController.getAllCollegePosts - 학과 전체 게시글 리스트 (Status: "ACTIVE")
    fun findAllByCollegeAndActiveStatusOrderByIdDesc(college: CollegeType, activeStatus: ActiveStatusType): List<Post>

    // MyPageController.getAllMyPosts - 자신이 쓴 게시글 리스트
    fun findByAuthorAndActiveStatusOrderByCreatedAtDesc(user: User, activeStatus: ActiveStatusType): List<Post>

    @Query(
            nativeQuery = true,
            value = "SELECT p.id, m.major_name, p.title FROM post p " +
                    "join user u on u.id = p.author_id " +
                    "join major m on m.id = u.major_id " +
                    "WHERE p.college = :college and p.active_status = :activeStatus " +
                    "order by p.created_at desc LIMIT 5"
    )
    fun findHomePostsByCollege(college: CollegeType, activeStatus: ActiveStatusType): List<PostsInHomeResponseDto.PostHomeDto>
}