package edu.handong.cseemileage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(basePackages = ["edu.handong.cseemileage"])
@SpringBootApplication
class SwMileageServerApplication

fun main(args: Array<String>) {
    runApplication<SwMileageServerApplication>(*args)
}
