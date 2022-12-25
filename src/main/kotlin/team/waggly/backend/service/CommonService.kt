package team.waggly.backend.service

import org.springframework.stereotype.Service
import team.waggly.backend.commomenum.ActiveStatusType
import team.waggly.backend.dto.profileImg.ProfileImgResponseDto
import team.waggly.backend.repository.ProfileImgRepository


@Service
class CommonService(
    private val profileImgRepository: ProfileImgRepository
) {
    // 기본 와글리 프로필 이미지
    fun getProfileImgs(): List<ProfileImgResponseDto> {
        val profileImgList = profileImgRepository.findAllByActiveStatusOrderByIdAsc(ActiveStatusType.ACTIVE)
        print(profileImgList)
        return profileImgList.map {
            ProfileImgResponseDto(
                id = it.id,
                url = it.url,
                color = it.color,
                colorNumber = it.colorNumber,
                activeStatus = it.activeStatus,
            )
        }
    }
}