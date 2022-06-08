package team.waggly.backend.service.emailService

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.javamail.MimeMessagePreparator
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import team.waggly.backend.dto.ResponseDto
import team.waggly.backend.dto.email.EmailRequestDto
import team.waggly.backend.repository.UniversityRepository
import java.util.concurrent.TimeUnit
import javax.mail.internet.MimeMessage

@Service
class SendEmailService(
    private val mailSender: JavaMailSender,
    private val redisTemplate: RedisTemplate<Any, Any>,
    private val universityRepository: UniversityRepository
)
{
    //Post 인증번호 전송 Controller Main 서비스 함수
    fun emailCertification(emailCertificationRequestDto: EmailRequestDto): ResponseDto<Any>{
        val email = emailCertificationRequestDto.email
        if(!universityEmailValidation(email))
            return ResponseDto(null,"이메일 형식이 학교이메일이 아닙니다.",404)

        val key = createKey()
        println(key)
        saveHashMapToRedis(email,key)
        certificationNumSender(email,key)

        return ResponseDto(null)
    }

    //학교이메일 유효성 검사 ac.kr , .edu 없으면 학교이메일 취금 x
    fun universityEmailValidation(email: String):Boolean{
        val isUniversityEmail = when{
            email.contains("ac.kr") -> true
            email.contains(".edu") -> true
            else -> false
        }
        return isUniversityEmail
    }

    // 인증번호가 포함된 이메일 보내는 함수
    fun certificationNumSender(email: String, key: String){
        try {
            val preparatory = MimeMessagePreparator { message: MimeMessage? ->
                val helper = MimeMessageHelper(message!!)
                helper.setTo(email)
                helper.setSubject("와글리 회원가입 인증번호 안내")
                helper.setText(key)
            }
            mailSender.send(preparatory)
        }catch (es: MailException){
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"이메일 전송 오류")
        }

    }

    // 랜덤 인증번호 만드는 함수
    fun createKey():String{
        val key = StringBuffer()
        val range = (0..2)

        for (i: Int in 0..5){
            val upperCase = ((Math.random()*26).toInt()+65).toChar()
            val lowerCase = ((Math.random()*26).toInt()+97).toChar()
            val randomNum = (0..9).random()

            when(range.random()){
                0 -> key.append(upperCase)
                1 -> key.append(lowerCase)
                2 -> key.append(randomNum)
            }
        }
        return key.toString()
    }

    //  Key = 이메일, value = 인증번호를 Redis에 expire time 100초로 저장시킨다.
    fun saveHashMapToRedis(email: String,certiNum: String){
        val hashOperations = redisTemplate.opsForValue()
        hashOperations.set(email,certiNum,100,TimeUnit.SECONDS)
    }
}