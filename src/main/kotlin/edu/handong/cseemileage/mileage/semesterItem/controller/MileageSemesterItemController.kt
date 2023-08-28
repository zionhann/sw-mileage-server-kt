package edu.handong.cseemileage.mileage.semesterItem.controller

import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemDto
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemForm
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemMultipleForm
import edu.handong.cseemileage.mileage.semesterItem.service.SemesterItemQueryService
import edu.handong.cseemileage.mileage.semesterItem.service.SemesterItemService
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
        return ResponseEntity.ok(SemesterItemDto(semesterItems = semesterItems))
    }

    /**
     * semesterItem > category, item, students 형식 반환
     * */
    @GetMapping("/{semesterName}/items/records")
    fun getSemesterItemsAndRecords(
        @PathVariable semesterName: String
    ): ResponseEntity<List<SemesterItemDto.InfoV4>> {
        return ResponseEntity.ok(semesterItemQueryService.getSemesterItemsWithRecords(semesterName))
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
        val removedId = semesterItemService.deleteSemesterItem(semesterItemId)
        return ResponseEntity.ok(mapOf("id" to removedId))
    }
}
