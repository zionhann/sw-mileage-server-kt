package edu.handong.cseemileage

import edu.handong.cseemileage.config.DataBaseProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(DataBaseProperties::class)
class SwMileageServerApplication

fun main(args: Array<String>) {
    runApplication<SwMileageServerApplication>(*args)
}
