package edu.handong.cseemileage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SwMileageServerApplication

fun main(args: Array<String>) {
    runApplication<SwMileageServerApplication>(*args)
}
