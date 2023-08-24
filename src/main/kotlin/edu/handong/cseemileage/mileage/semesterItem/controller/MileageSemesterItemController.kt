package edu.handong.cseemileage.mileage.semesterItem.controller

import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemDto
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemForm
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemMultipleForm
import edu.handong.cseemileage.mileage.semesterItem.service.SemesterItemQueryService
import edu.handong.cseemileage.mileage.semesterItem.service.SemesterItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/mileage/semesterItems")
class MileageSemesterItemController @Autowired constructor(
    val semesterItemService: SemesterItemService,
    val semesterItemQueryService: SemesterItemQueryService
) {

    companion object {
        private const val API_URI = "/api/mileage/semesters"
    }

    @PostMapping
    fun createSemester(
        @RequestBody @Valid
        form: SemesterItemForm
    ): ResponseEntity<String> {
        semesterItemService.saveSemesterItem(form)
        return ResponseEntity.created(
            URI.create(API_URI)
        ).body("학기별 항목 생성 완료")
    }

    /**
     * 학기 별 항목 생성 insert 최적화
     * */
    @PostMapping("/multiple")
    fun createSemesterMultiple(
        @RequestBody @Valid
        form: SemesterItemMultipleForm
    ): ResponseEntity<String> {
        semesterItemService.saveSemesterItemMultipleBulkInsert(form)
        return ResponseEntity.created(
            URI.create("$API_URI/multiple")
        ).body("학기별 항목 생성(multiple) 완료")
    }

    /**
     * item / category 형식 반환
     * */
    @GetMapping
    fun getSemestersByName(
        @RequestParam(required = false) semester: String
    ): ResponseEntity<SemesterItemDto> {
        // Todo: filter 사용(학기, 항목명 등)
        val semesterItems = semesterItemQueryService.getSemesterItemsV1(semester)
        return ResponseEntity.ok(SemesterItemDto(semesterItems = semesterItems))
    }
}
