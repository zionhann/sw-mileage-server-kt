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
import org.springframework.web.bind.annotation.RequestParam
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

    @Deprecated(message = "글로벌 카테고리, 항목 join 조회 사용 예정")
    @GetMapping
    fun getCategories(): ResponseEntity<CategoryDto> {
        val categories = categoryQueryService.getCategories()
        return ResponseEntity.ok(CategoryDto(categories = categories))
    }

    @GetMapping("/global")
    fun getCategoriesWithItems(): ResponseEntity<CategoryDto> {
        val categories = categoryQueryService.getCategoriesWithItems()
        return ResponseEntity.ok(CategoryDto(categoriesWithItems = categories))
    }

    /**
     * 학기별 카테고리, 항목, 학기 정보 조회
     * */
    @GetMapping("/v2")
    fun getItems(
        @RequestParam("semester") semester: String
    ): ResponseEntity<CategoryDto> {
        val list = categoryQueryService.getCategoryWithItemAndSemester(semester)
        return ResponseEntity.ok(CategoryDto(categoriesWithItemsAndSemesters = list))
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
