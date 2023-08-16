package edu.handong.cseemileage.mileage.category.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MileageCategoryController {

    @GetMapping("/")
    fun index(): String {
        return "Index Controller"
    }
}
