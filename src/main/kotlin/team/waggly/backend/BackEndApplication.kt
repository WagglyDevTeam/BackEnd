package team.waggly.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = arrayOf(SecurityAutoConfiguration::class))
class BackEndApplication

fun main(args: Array<String>) {
    runApplication<BackEndApplication>(*args)
}