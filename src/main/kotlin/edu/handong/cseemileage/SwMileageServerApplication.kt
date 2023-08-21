package edu.handong.cseemileage

import edu.handong.cseemileage.config.DataBaseProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(basePackages = ["edu.handong.cseemileage"])
@SpringBootApplication
@EnableConfigurationProperties(DataBaseProperties::class)
class SwMileageServerApplication

fun main(args: Array<String>) {
    runApplication<SwMileageServerApplication>(*args)
}
