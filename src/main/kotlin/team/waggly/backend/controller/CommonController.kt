package team.waggly.backend.controller

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ListObjectsRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*

@RestController
class CommonController(private val amazonS3Client: AmazonS3Client){
    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    @Value("\${cloud.aws.s3.dir}")
    lateinit var dir: String

    @GetMapping("/profile-images/default")
    fun getDefaultProfileImages(): String {
//        val request = ListObjectsRequest(bucket, dir)
//        val urls = amazonS3Client.get
//        print(urls)
//        return urls.toString()
    }
}