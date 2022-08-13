package team.waggly.backend.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.commomenum.CollegeType
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

    fun findAllByCollegeAndActiveStatusOrderByCreatedAtDesc(college: CollegeType, activeStatus: ActiveStatusType, pageable: Pageable): List<Post>
}