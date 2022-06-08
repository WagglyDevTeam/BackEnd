package team.waggly.backend.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AmazonS3Config {
    @Value("\${cloud.aws.region.static}")
    lateinit var region: String

    @Bean
    fun amazonS3Client(): AmazonS3Client {
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(DefaultAWSCredentialsProviderChain())
                .build() as AmazonS3Client
    }
}