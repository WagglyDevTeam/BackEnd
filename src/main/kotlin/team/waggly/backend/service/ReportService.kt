package team.waggly.backend.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.report.ReportRequestDto
import team.waggly.backend.repository.*
import javax.transaction.Transactional

@Service
class ReportService(
        private val reportRepository: ReportRepository,
        private val userRepository: UserRepository,
        private val postRepository: PostRepository,
        private val commentRepository: CommentRepository,
        private val groupChatRepository: GroupChatRepository,
        private val chatRepository: GroupChatRepository,
) {
    @Transactional
    fun createReportForPost(reportRequestDto: ReportRequestDto): ResponseDto<Any> {
        val post = postRepository.findByIdOrNull(reportRequestDto.id)
                ?: throw IllegalArgumentException("존재하지 않는 게시글입니다.")
        val reportedUser = userRepository.findByIdOrNull(reportRequestDto.reportedUserId)
                ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")

        reportRepository.save(reportRequestDto.generateReport())
        return ResponseDto()
    }

    @Transactional
    fun createReportForComment(reportRequestDto: ReportRequestDto): ResponseDto<Any> {
        val comment = commentRepository.findByIdOrNull(reportRequestDto.id)
                ?: throw IllegalArgumentException("존재하지 않는 댓글입니다.")
        val reportedUser = userRepository.findByIdOrNull(reportRequestDto.reportedUserId)
                ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")

        reportRepository.save(reportRequestDto.generateReport())
        return ResponseDto()
    }

    @Transactional
    fun createReportForGroupChatUser(reportRequestDto: ReportRequestDto): ResponseDto<Any> {
        val groupchat = groupChatRepository.findByIdOrNull(reportRequestDto.id)
                ?: throw IllegalArgumentException("존재하지 않는 그룹 채팅방입니다.")
        val reportedUser = userRepository.findByIdOrNull(reportRequestDto.reportedUserId)
                ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")

        reportRepository.save(reportRequestDto.generateReport())
        return ResponseDto()
    }

    @Transactional
    fun createReportForChatUser(reportRequestDto: ReportRequestDto): ResponseDto<Any> {
        val chat = chatRepository.findByIdOrNull(reportRequestDto.id)
                ?: throw IllegalArgumentException("존재하지 않는 1:1 채팅방입니다.")
        val reportedUser = userRepository.findByIdOrNull(reportRequestDto.reportedUserId)
                ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")

        reportRepository.save(reportRequestDto.generateReport())
        return ResponseDto()
    }
}