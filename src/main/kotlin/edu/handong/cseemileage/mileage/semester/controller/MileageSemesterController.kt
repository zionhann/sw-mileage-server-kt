package edu.handong.cseemileage.mileage.semester.controller

import edu.handong.cseemileage.mileage.semester.dto.SemesterDto
import edu.handong.cseemileage.mileage.semester.dto.SemesterForm
import edu.handong.cseemileage.mileage.semester.dto.SemesterMultipleForm
import edu.handong.cseemileage.mileage.semester.service.SemesterQueryService
import edu.handong.cseemileage.mileage.semester.service.SemesterService
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
@RequestMapping("/api/mileage/semesters")
class MileageSemesterController @Autowired constructor(
    val semesterService: SemesterService,
    val semesterQueryService: SemesterQueryService
) {

    companion object {
        private const val API_URI = "/api/mileage/semesters"
    }

    @PostMapping
    fun createSemester(
        @RequestBody @Valid
        form: SemesterForm
    ): ResponseEntity<String> {
        semesterService.saveSemester(form)
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
        form: SemesterMultipleForm
    ): ResponseEntity<String> {
        semesterService.saveSemesterMultipleBulkInsert(form)
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
    ): ResponseEntity<SemesterDto> {
        // Todo: filter 사용(학기, 항목명 등)
        val semesterItems = semesterQueryService.getSemestersV1(semester)
        return ResponseEntity.ok(SemesterDto(semesters = semesterItems))
    }
}
