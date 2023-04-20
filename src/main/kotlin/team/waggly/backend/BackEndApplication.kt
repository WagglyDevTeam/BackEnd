package team.waggly.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class BackEndApplication

fun main(args: Array<String>) {
    runApplication<BackEndApplication>(*args)
}
