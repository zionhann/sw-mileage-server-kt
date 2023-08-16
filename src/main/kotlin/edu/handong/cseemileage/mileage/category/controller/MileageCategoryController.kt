package edu.handong.cseemileage.mileage.category.controller

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import edu.handong.cseemileage.mileage.category.service.CategoryQueryService
import edu.handong.cseemileage.mileage.category.service.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/mileage/categories")
class MileageCategoryController @Autowired constructor(
    val categoryService: CategoryService,
    val categoryQueryService: CategoryQueryService
) {
    @PostMapping
    fun createCategory(
        @RequestBody @Valid
        form: CategoryForm
    ): ResponseEntity<Map<String, Int>> {
        val savedId = categoryService.saveCategory(form)
        return ResponseEntity.created(
            URI.create("/api/mileage/categories/$savedId")
        ).body(mapOf("id" to savedId))
    }

    @GetMapping
    fun getCategories(): ResponseEntity<CategoryDto> {
        val categories = categoryQueryService.getCategories()
        return ResponseEntity.ok(CategoryDto(categories))
    }

    @PatchMapping("/{id}")
    fun updateCategory(
        @RequestBody @Valid
        form: CategoryForm,
        @PathVariable id: Int
    ): ResponseEntity<Map<String, Int>> {
        val updatedId = categoryService.update(id, form)
        return ResponseEntity.ok(mapOf("id" to updatedId))
    }

    @DeleteMapping("/{id}")
    fun removeCategory(
        @PathVariable id: Int
    ): ResponseEntity<Map<String, Int>> {
        val removedId = categoryService.remove(id)
        return ResponseEntity.ok(mapOf("id" to removedId))
    }
}
