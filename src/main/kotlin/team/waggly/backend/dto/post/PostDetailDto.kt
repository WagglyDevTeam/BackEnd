package team.waggly.backend.dto.post

import com.fasterxml.jackson.annotation.JsonProperty
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.model.Post
import java.time.LocalDateTime

data class PostDetailDto(
    val postId: Long?,
    val postTitle: String,
    val postDesc: String,
    val postCreatedAt: LocalDateTime,
    val postImages: MutableList<String>,
    var postLikeCnt: Int?,
    var postCommentCnt: Int?,
    @get:JsonProperty("isLikedByMe")
    var isLikedByMe: Boolean?,
    val authorId: Long,
    var authorMajor: String,
    var authorNickname: String,
    var authorProfileImg: String,
    @get:JsonProperty("isBlind")
    val isBlind: Boolean,
    @get:JsonProperty("isAnonymous")
    val isAnonymous: Boolean,
) {
    constructor(post: Post): this(
        post.id,
        post.title,
        post.description,
        post.createdAt,
        arrayListOf<String>(),
        null,
        null,
        null,
        post.author.id!!,
        post.author.major.majorName,
        post.author.nickName,
        post.author.profileImgUrl,
        post.activeStatus == ActiveStatusType.INACTIVE,
        post.isAnonymous != 0,
    )

    fun checkIsAnonymous() {
        if (this.isAnonymous) {
            this.authorNickname = "익명"
            this.authorProfileImg = "https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/309/59932b0eb046f9fa3e063b8875032edd_crop.jpeg"
        }
    }
}