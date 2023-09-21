package edu.handong.cseemileage.controller

import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemDto
import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemForm
import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemMultipleForm
import edu.handong.cseemileage.exception.mileage.semesterItem.SemesterItemCannotDeleteException
import edu.handong.cseemileage.service.semesterItem.SemesterItemQueryService
import edu.handong.cseemileage.service.semesterItem.SemesterItemService
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
@RequestMapping("/api/mileage/semesters")
@CrossOrigin(origins = ["*"])
class MileageSemesterItemController(
    val semesterItemService: SemesterItemService,
    val semesterItemQueryService: SemesterItemQueryService
) {

    companion object {
        private const val API_URI = "/api/mileage/semesters"
    }

    @PostMapping("/{semesterName}/items")
    fun createSemester(
        @RequestBody @Valid
        form: SemesterItemForm,
        @PathVariable semesterName: String
    ): ResponseEntity<Map<String, Int>> {
        val savedId = semesterItemService.saveSemesterItem(form, semesterName)
        return ResponseEntity.created(
            URI.create("$API_URI/$semesterName/items/$savedId")
        ).body(mapOf("id" to savedId))
    }

    /**
     * 학기 별 항목 생성 insert 최적화
     * */
    @PostMapping("/{semesterName}/multiple")
    fun createSemesterMultiple(
        @RequestBody @Valid
        form: SemesterItemMultipleForm,
        @PathVariable semesterName: String
    ): ResponseEntity<String> {
        semesterItemService.saveSemesterItemMultipleBulkInsert(form, semesterName)
        return ResponseEntity.created(
            URI.create("$API_URI/$semesterName/items/multiple")
        ).body("학기별 항목 생성(multiple) 완료")
    }

    /**
     * item / category 형식 반환
     * */
    @GetMapping("/{semesterName}/items")
    fun getSemestersByName(
        @PathVariable semesterName: String
    ): ResponseEntity<SemesterItemDto> {
        // Todo: filter 사용(학기, 항목명 등)
        val semesterItems = semesterItemQueryService.getSemesterItemsV1(semesterName)
        return ResponseEntity.ok(
            SemesterItemDto(
                list = semesterItems,
                count = semesterItems.size,
                description = "학기별 항목 조회 결과 filtered by $semesterName"
            )
        )
    }

    /**
     * semesterItem > category, item, students 형식 반환
     * */
    @GetMapping("/{semesterName}/items/records")
    fun getSemesterItemsAndRecords(
        @PathVariable semesterName: String
    ): ResponseEntity<SemesterItemDto> {
        val semesterItems = semesterItemQueryService.getSemesterItemsWithRecords(semesterName)
        return ResponseEntity.ok(
            SemesterItemDto(
                list = semesterItems,
                count = semesterItems.size,
                description = "학기별 항목 조회 join with 카테고리, 항목, 마일리지 기록, 학생"
            )
        )
    }

    @GetMapping("/items/{itemId}")
    fun getSemesterItemByItemId(
        @PathVariable itemId: Int
    ): ResponseEntity<SemesterItemDto> {
        val semesterItems = semesterItemQueryService.getSemesterItemByItemId(itemId)
        return ResponseEntity.ok(
            SemesterItemDto(
                list = semesterItems,
                count = semesterItems.size,
                description = "학기별 항목 조회 by itemId($itemId)"
            )
        )
    }

    @PatchMapping("/{semesterItemId}")
    fun modifyItem(
        @PathVariable semesterItemId: Int,
        @RequestBody @Valid
        form: SemesterItemForm
    ): ResponseEntity<Map<String, Int>> {
        val modifiedId = semesterItemService.modifySemesterItem(semesterItemId, form)
        return ResponseEntity.ok(mapOf("id" to modifiedId))
    }

    @DeleteMapping("/{semesterItemId}")
    fun deleteItem(
        @PathVariable
        semesterItemId: Int
    ): ResponseEntity<Map<String, Int>> {
        return try {
            val removedId = semesterItemService.deleteSemesterItem(semesterItemId)
            ResponseEntity.ok(mapOf("id" to removedId))
        } catch (e: DataIntegrityViolationException) {
            throw SemesterItemCannotDeleteException()
        }
    }
}
