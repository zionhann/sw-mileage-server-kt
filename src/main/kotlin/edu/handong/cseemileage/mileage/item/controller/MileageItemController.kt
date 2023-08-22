package edu.handong.cseemileage.mileage.item.controller

import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.item.dto.ItemForm
import edu.handong.cseemileage.mileage.item.service.ItemQueryService
import edu.handong.cseemileage.mileage.item.service.ItemService
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
import javax.validation.Valid

@RestController
@RequestMapping("/api/mileage/items")
class MileageItemController @Autowired constructor(
    val itemService: ItemService,
    val itemQueryService: ItemQueryService
) {
    @PostMapping
    fun createItem(
        @RequestBody @Valid
        form: ItemForm
    ): Int {
        return itemService.saveItem(form)
    }

    @GetMapping
    fun getItems(): ResponseEntity<ItemDto> {
        val items = itemQueryService.getItems()
        return ResponseEntity.ok(ItemDto(items = items))
    }

    @PatchMapping("/{subitemId}")
    fun modifyItem(
        @PathVariable("subitemId")
        itemId: Int,
        @RequestBody @Valid
        form: ItemForm
    ) {
        return itemService.modifyItem(itemId, form)
    }

    @DeleteMapping("/{subitemId}")
    fun deleteItem(
        @PathVariable("subitemId")
        itemId: Int
    ) {
        return itemService.deleteItem(itemId)
    }
}
