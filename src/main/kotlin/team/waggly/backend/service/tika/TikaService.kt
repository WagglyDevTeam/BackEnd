package team.waggly.backend.service.tika

import org.apache.tika.Tika
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class TikaService {
    val tika = Tika()
    fun mimeType(file: MultipartFile): String {
        return tika.detect(file.inputStream)
    }

    fun typeCheck(file: MultipartFile): Boolean {
        val allowType = arrayOf("image/png", "image/jpeg", "image/bmp", "image/gif")
        val fileType = tika.detect(file.inputStream)
        return allowType.contains(fileType)
    }
}