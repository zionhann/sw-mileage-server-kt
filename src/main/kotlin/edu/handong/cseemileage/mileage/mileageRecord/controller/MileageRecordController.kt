package edu.handong.cseemileage.mileage.mileageRecord.controller

import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordDto
import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordForm
import edu.handong.cseemileage.mileage.mileageRecord.service.MileageRecordQueryService
import edu.handong.cseemileage.mileage.mileageRecord.service.MileageRecordService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/mileage/records")
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
        return ResponseEntity.ok(MileageRecordDto(mileageRecordQueryService.getAll()))
    }
}
