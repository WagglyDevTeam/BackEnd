package team.waggly.backend.service.awsS3

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.util.IOUtils
import java.io.ByteArrayInputStream
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Component
class S3Uploader(
        private val amazonS3Client: AmazonS3Client
) {
    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    @Value("\${cloud.aws.s3.dir}")
    lateinit var dir: String

    fun upload(file: MultipartFile): String {
        val s3Object = makeS3ObjectName(file.originalFilename!!)

        val bytes = IOUtils.toByteArray(file.inputStream)
        val byteArray = ByteArrayInputStream(bytes)

        val metadata = ObjectMetadata()
        metadata.contentLength = bytes.size.toLong()
        metadata.contentType = file.contentType

        amazonS3Client.putObject(PutObjectRequest(bucket, s3Object, byteArray, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead))

        return amazonS3Client.getUrl(bucket, s3Object).toString()
    }

    fun delete(fileName: String) {
//        val deleteObjectRequest = DeleteObjectRequest(bucket, (dir + fileName))
        amazonS3Client.deleteObject(bucket, dir + fileName)
    }

    private fun makeS3ObjectName(filename: String): String {
        return "newsroom/${UUID.randomUUID()}.${filename.substringAfterLast(".")}"
    }
}