package team.waggly.backend.service.emailService

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import team.waggly.backend.dto.certification.CertificationRequestDto
import team.waggly.backend.dto.certification.CertificationResponseDto
import team.waggly.backend.model.University
import team.waggly.backend.repository.UniversityRepository
import team.waggly.backend.repository.UserRepository
import team.waggly.backend.security.UserDetailsImpl
import team.waggly.backend.security.jwt.JwtTokenUtils

@Service
class CertificationService(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val universityRepository: UniversityRepository,
    private val jwtTokenUtils: JwtTokenUtils,
    private val userRepository: UserRepository,
){
    fun certificationEmail(certificationReqeustDto: CertificationRequestDto): CertificationResponseDto {
        val email = certificationReqeustDto.email
        val certiNum = certificationReqeustDto.certiNum
        val university = getUniversityName(getUniversityEmail(email)) ?: throw Exception("일치하는 학교가 없습니다.")
        val user = userRepository.findByEmail(email)
        var token: String? = null
        if (user != null) {
            token = jwtTokenUtils.generateJwtToken(UserDetailsImpl(user))
        }

        return when(checkCertificationNum(email,certiNum)){
            true -> CertificationResponseDto( universityId = university.Id!!, university = university.universityName, token = token)
            false -> throw Exception("인증번호가 일치하지 않거나 유효시간이 지났습니다.")
        }

    }

    fun getUniversityName(universityEmail: String): University? {
        return universityRepository.findByUniversityEmail(universityEmail)
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
        val hashOperations = redisTemplate.opsForValue()
        print(hashOperations.get(email))
        if(hashOperations.get(email)!=null)
            if(certiNum == hashOperations.get(email))
                return true

        return false
    }
}