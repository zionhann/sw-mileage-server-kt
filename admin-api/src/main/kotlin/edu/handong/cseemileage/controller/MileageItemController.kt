package edu.handong.cseemileage.controller

import edu.handong.cseemileage.dto.mileage.category.CategoryDto
import edu.handong.cseemileage.dto.mileage.item.ItemDto
import edu.handong.cseemileage.dto.mileage.item.ItemForm
import edu.handong.cseemileage.exception.mileage.item.ItemCannotDeleteException
import edu.handong.cseemileage.service.item.ItemQueryService
import edu.handong.cseemileage.service.item.ItemService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "항목 API")
class MileageItemController(
    val itemService: ItemService,
    val itemQueryService: ItemQueryService
) {
    @Operation(summary = "마일리지 항목 생성", description = "관리자 사이트에서 마일리지 항목을 생성합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "항목 생성 성공"),
            ApiResponse(responseCode = "400", description = "항목 categoryId, name 누락, categoryId 0 또는 음수, stuType 패턴 불일치, itemMaxPoints 음수 등 항목 생성 실패"),
            ApiResponse(responseCode = "500", description = "이미 존재하는 항목, 존재하지 않는 카테고리 등의 이유로 항목 생성 실패")
        ]
    )
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

    @Operation(summary = "마일리지 항목 전체 조회", description = "관리자 사이트에서 마일리지 항목 전체를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "마일리지 항목 전체 조회 성공")
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

    @Operation(summary = "항목 필터링 조회 by categoryId(카테고리 PK)", description = "카테고리 하위의 항목 목록을 필터링해서 조회합니다. 없을 경우 빈리스트를 반환합니다.")
    @Parameter(name = "categoryId", description = "카테고리 PK", required = true, example = "1")
    @ApiResponse(responseCode = "200", description = "항목 필터링 조회 성공 by categoryId")
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

    @Operation(summary = "글로벌 목록 조회(카테고리, 항목)", description = "학기별로 달라지지 않는 글로벌 카테고리와 항목을 함께 조회합니다. list{카테고리 {항목 리스트} 리스트} 형식으로 반환됩니다")
    @ApiResponse(responseCode = "200", description = "글로벌 목록 조회 성공")
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

    @Operation(summary = "항목 수정 by itemId(항목 PK)", description = "하나의 항 정보를 수정합니다. name 정보는 필수로 요청해야 하며 나머지 정보는 수정을 원하는 경우 요청합니다.")
    @Parameter(name = "itemId", description = "항목 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "항목 수정 성공"),
            ApiResponse(responseCode = "400", description = "항목 name 누락, categoryId 음수, stuType 패턴(C, F, CF) 불일치, itemMaxPoints 음수 등의 이유로 항목 수정 실패"),
            ApiResponse(responseCode = "500", description = "항목 name 중복, 존재하지 않는 항목, 존재하지 않는 카테고리 등의 이유로 항목 수정 실패")
        ]
    )
    @PatchMapping("/{itemId}")
    fun modifyItem(
        @PathVariable itemId: Int,
        @RequestBody @Valid
        form: ItemForm
    ): ResponseEntity<Map<String, Int>> {
        val modifiedId = itemService.modifyItem(itemId, form)
        return ResponseEntity.ok(mapOf("id" to modifiedId))
    }

    @Operation(summary = "항목 삭제 by itemId(항목 PK)", description = "항목을 삭제 합니다. 해당 항목을 참조하는 학기별 항목이 있을 경우 삭제 불가능합니다.")
    @Parameter(name = "itemId", description = "항목 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "항목 삭제 성공"),
            ApiResponse(responseCode = "500", description = "itemId에 해당하는 항목이 존재하지 않음, 항목을 참조하는 학기별 항목이 존재함 등의 이유로 삭제 실패")
        ]
    )
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
