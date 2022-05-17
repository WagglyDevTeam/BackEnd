package team.waggly.backend.service.emailService

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import team.waggly.backend.dto.CertificationDto
import team.waggly.backend.repository.UniversityRepository

@Service
class CertificationService(
    private val redisTemplate: RedisTemplate<Any, Any>,
    private val universityRepository: UniversityRepository
){
    fun certificationEmail(certificationReqeustDto: CertificationDto.Reqeust): CertificationDto.Response {
        val email = certificationReqeustDto.email
        val certiNum = certificationReqeustDto.certiNum
        val universityName = getUniversityName(getUniversityEmail(email))

        return when(checkCertificationNum(email,certiNum)){
            true -> CertificationDto.Response(true,universityName)
            false -> CertificationDto.Response(false,universityName)
        }

    }

    fun getUniversityName(universityEmail: String): String {
        val university = universityRepository.findByUniversityEmail(universityEmail)
        if (university != null) {
            return university.universityName
        }
        else{
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"해당되는 학교이름이 없습니다.")
        }
    }

    // 유형에 따라 추가될수있음
    fun getUniversityEmail(email: String): String{
        var splitAtEmail = email.split("@")[1]

        if (splitAtEmail.contains("gs."))
            splitAtEmail = splitAtEmail.split("gs.")[1]
        if(splitAtEmail.contains(".edu"))
            splitAtEmail = splitAtEmail.split(".edu")[0]
        if(splitAtEmail.contains(".ac.kr"))
            splitAtEmail = splitAtEmail.split(".ac.kr")[0]

        return splitAtEmail
    }

    fun checkCertificationNum(email: String, certiNum: String): Boolean{
        val hashOperations = redisTemplate.opsForHash<String, Any>()
        val hashMapForCertification = hashOperations.entries("certificationEmail")

        if(hashMapForCertification.containsKey(email))
            if(certiNum == hashMapForCertification[email])
                return true

        return false
    }
}