package edu.handong.cseemileage.mileage.mileageRecord.controller

import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordDto
import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordForm
import edu.handong.cseemileage.mileage.mileageRecord.service.MileageRecordQueryService
import edu.handong.cseemileage.mileage.mileageRecord.service.MileageRecordService
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
@RequestMapping("/api/mileage/records")
@CrossOrigin(origins = ["*"])
class MileageRecordController(
    private val mileageRecordService: MileageRecordService,
    private val mileageRecordQueryService: MileageRecordQueryService
) {

    @PostMapping
    fun createRecord(
        @Valid @RequestBody
        form: MileageRecordForm
    ): ResponseEntity<Map<String, Int>> {
        val added = mileageRecordService.add(form)

        return ResponseEntity.created(
            URI.create("/api/mileage/records/$added")
        ).body(
            mapOf("id" to added)
        )
    }

    @GetMapping
    fun readRecords(): ResponseEntity<MileageRecordDto> {
        val records = mileageRecordQueryService.getRecords()
        return ResponseEntity.ok(
            MileageRecordDto(
                list = records,
                count = records.size,
                description = "마일리지 기록 전체 조회 결과"
            )
        )
    }

    @GetMapping("/filter")
    fun readRecordsBy(
        @RequestParam(required = true) semesterItemId: Int
    ): ResponseEntity<MileageRecordDto> {
        val records = mileageRecordQueryService.getRecordsBy(semesterItemId)
        return ResponseEntity.ok(
            MileageRecordDto(
                list = records,
                count = records.size,
                description = "마일리지 기록 전체 조회 결과 filtered by (semesterItemId = $semesterItemId)"
            )
        )
    }

    @GetMapping("/students/{sid}")
    fun readRecordsBySid(
        @PathVariable sid: String
    ): ResponseEntity<MileageRecordDto> {
        val records = mileageRecordQueryService.getRecordsByStudentId(sid)
        return ResponseEntity.ok(
            MileageRecordDto(
                list = records,
                count = records.size,
                description = "학생 별로 마일리지 기록 조회 결과 (sid = $sid)"
            )
        )
    }

    @GetMapping("/semesterItems/{semesterItemId}")
    fun readRecordsBySemesterItemId(
        @PathVariable semesterItemId: Int
    ): ResponseEntity<MileageRecordDto> {
        val records = mileageRecordQueryService.getRecordsBySemesterItemId(semesterItemId)
        return ResponseEntity.ok(
            MileageRecordDto(
                list = records,
                count = records.size,
                description = "학기 항목 별로 마일리지 기록 조회한 결과"
            )
        )
    }

    @PatchMapping("/{mileageRecordId}")
    fun modifyMileageRecord(
        @PathVariable mileageRecordId: Int,
        @RequestBody @Valid
        form: MileageRecordForm
    ): ResponseEntity<Map<String, Int>> {
        val modifiedId = mileageRecordService.modifyMileageRecord(mileageRecordId, form)
        return ResponseEntity.ok(mapOf("id" to modifiedId))
    }

    @DeleteMapping("/{mileageRecordId}")
    fun deleteItem(
        @PathVariable
        mileageRecordId: Int
    ): ResponseEntity<Map<String, Int>> {
        val removedId = mileageRecordService.deleteMileageRecord(mileageRecordId)
        return ResponseEntity.ok(mapOf("id" to removedId))
    }
}
