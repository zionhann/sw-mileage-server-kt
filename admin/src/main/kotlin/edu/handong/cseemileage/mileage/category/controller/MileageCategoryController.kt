package edu.handong.cseemileage.mileage.category.controller

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.dto.CategoryForm
import edu.handong.cseemileage.mileage.category.exception.CategoryCannotDeleteException
import edu.handong.cseemileage.mileage.category.service.CategoryQueryService
import edu.handong.cseemileage.mileage.category.service.CategoryService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
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
@CrossOrigin(origins = ["*"])
class MileageCategoryController(
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
        return ResponseEntity.ok(
            CategoryDto(
                list = categories,
                count = categories.size,
                description = "카테고리 목록 조회 결과"
            )
        )
    }

    @GetMapping("{categoryId}")
    fun getOneCategory(
        @PathVariable categoryId: Int
    ): ResponseEntity<CategoryDto> {
        val category = categoryQueryService.getCategoryById(categoryId)
        return ResponseEntity.ok(
            CategoryDto(
                data = category,
                description = "카테고리 단건 조회 by categoryId ( = $categoryId )"
            )
        )
    }

//    @GetMapping("/global")
//    fun getCategoriesWithItems(): ResponseEntity<CategoryDto> {
//        val categories = categoryQueryService.getCategoriesWithItems()
//        return ResponseEntity.ok(
//            CategoryDto(
//                list = categories,
//                count = categories.size,
//                description = "글로벌 카테고리, 항목 join 조회 결과"
//            )
//        )
//    }

    /**
     * 학기별 카테고리, 항목, 학기 정보 조회
     * */
    @GetMapping("/v2")
    fun getItems(
        @RequestParam("semester") semester: String
    ): ResponseEntity<CategoryDto> {
        val list = categoryQueryService.getCategoryWithItemAndSemester(semester)
        return ResponseEntity.ok(
            CategoryDto(
                list = list,
                count = list.size,
                description = "학기별로 카테고리, 항목, 학기별 항목 정보 조회한 결과"
            )
        )
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
        try {
            val removedId = categoryService.remove(id)
            return ResponseEntity.ok(mapOf("id" to removedId))
        } catch (e: DataIntegrityViolationException) {
            throw CategoryCannotDeleteException()
        }
    }
}
