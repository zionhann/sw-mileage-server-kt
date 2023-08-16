package edu.handong.cseemileage.mileage.category.controller

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import edu.handong.cseemileage.mileage.category.service.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/mileage/categories")
class MileageCategoryController @Autowired constructor(
    val categoryService: CategoryService
) {
    @PostMapping
    fun createCategory(
        @RequestBody @Valid
        form: CategoryForm
    ): ResponseEntity<CategoryDto.Info> {
        val saved = categoryService.saveCategory(form)
        return ResponseEntity.ok(saved)
    }

    @GetMapping
    fun getCategories(): ResponseEntity<CategoryDto> {
        val categories = categoryService.getCategories()
        return ResponseEntity.ok(CategoryDto(categories))
    }
}
