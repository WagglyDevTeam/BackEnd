package team.waggly.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.model.ProfileImg

@Repository
interface ProfileImgRepository: JpaRepository<ProfileImg, Long> {
    fun findAllByActiveStatusOrderByIdAsc(activeStatus: ActiveStatusType): List<ProfileImg>
}