package team.waggly.backend.service.awsS3

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
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
        private val S3Client: AmazonS3Client
) {
    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    @Value("\${cloud.aws.s3.dir}")
    lateinit var dir: String

    fun upload(file: MultipartFile): String {
        val split: List<String> = file.originalFilename!!.split(".")
        val extName: String = split[split.lastIndex]
        val fileName = "${UUID.randomUUID()}.${extName}"
        val objMeta = ObjectMetadata()
        val bytes = IOUtils.toByteArray(file.inputStream)
        objMeta.contentLength = bytes.size.toLong()

        val byteArrayIs = ByteArrayInputStream(bytes)

        S3Client.putObject(PutObjectRequest(bucket, dir + fileName, byteArrayIs, objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead))

        return S3Client.getUrl(bucket, dir + fileName).toString()
    }

    fun delete(fileName: String) {
        val deleteObjectRequest: DeleteObjectRequest = DeleteObjectRequest(bucket, dir + fileName)
        S3Client.deleteObject(deleteObjectRequest)
    }
}