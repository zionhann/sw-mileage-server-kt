package edu.handong.cseemileage.mileage.item.controller

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.item.dto.ItemForm
import edu.handong.cseemileage.mileage.item.exception.ItemCannotDeleteException
import edu.handong.cseemileage.mileage.item.service.ItemQueryService
import edu.handong.cseemileage.mileage.item.service.ItemService
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
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/mileage/items")
@CrossOrigin(origins = ["*"])
class MileageItemController(
    val itemService: ItemService,
    val itemQueryService: ItemQueryService
) {
    @PostMapping
    fun createItem(
        @RequestBody @Valid
        form: ItemForm
    ): ResponseEntity<Map<String, Int>> {
        val savedId = itemService.saveItem(form)
        return ResponseEntity.created(
            URI.create("/api/mileage/items/$savedId")
        ).body(mapOf("id" to savedId))
    }

    @GetMapping
    fun getItems(): ResponseEntity<ItemDto> {
        val items = itemQueryService.getItems()
        return ResponseEntity.ok(
            ItemDto(
                list = items,
                count = items.size,
                description = "세부 항목 목록 조회 결과"
            )
        )
    }

    @GetMapping("/categories/{categoryId}")
    fun getItemsByCategoryId(
        @PathVariable categoryId: Int
    ): ResponseEntity<ItemDto> {
        val items = itemQueryService.getItemsByCategoryId(categoryId)
        return ResponseEntity.ok(
            ItemDto(
                list = items,
                count = items.size,
                description = "카테고리 별 세부 항목 목록 조회 결과"
            )
        )
    }

    @GetMapping("/global")
    fun getItemsWithCategories(): ResponseEntity<CategoryDto> {
        val list = itemQueryService.getItemsWithCategory()
        return ResponseEntity.ok(
            CategoryDto(
                list = list,
                count = list.size,
                description = "글로벌 카테고리, 항목 join 조회 결과"
            )
        )
    }

    @PatchMapping("/{itemId}")
    fun modifyItem(
        @PathVariable itemId: Int,
        @RequestBody @Valid
        form: ItemForm
    ): ResponseEntity<Map<String, Int>> {
        val modifiedId = itemService.modifyItem(itemId, form)
        return ResponseEntity.ok(mapOf("id" to modifiedId))
    }

    @DeleteMapping("/{itemId}")
    fun deleteItem(
        @PathVariable
        itemId: Int
    ): ResponseEntity<Map<String, Int>> {
        try {
            val removedId = itemService.deleteItem(itemId)
            return ResponseEntity.ok(mapOf("id" to removedId))
        } catch (e: DataIntegrityViolationException) {
            throw ItemCannotDeleteException()
        }
    }
}
