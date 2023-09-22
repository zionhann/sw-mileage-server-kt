package edu.handong.cseemileage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories
@SpringBootApplication
class SwMileageStudentApplication

fun main(args: Array<String>) {
    runApplication<SwMileageStudentApplication>(*args)
}
