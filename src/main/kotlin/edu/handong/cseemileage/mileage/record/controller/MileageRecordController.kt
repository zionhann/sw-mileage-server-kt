package edu.handong.cseemileage.mileage.record.controller

import edu.handong.cseemileage.mileage.record.dto.RecordDto
import edu.handong.cseemileage.mileage.record.dto.RecordForm
import edu.handong.cseemileage.mileage.record.service.RecordQueryService
import edu.handong.cseemileage.mileage.record.service.RecordService
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
    private val recordService: RecordService,
    private val recordQueryService: RecordQueryService
) {

    @PostMapping
    fun createRecord(
        @Valid @RequestBody
        form: RecordForm
    ): ResponseEntity<Map<String, Int>> {
        val added = recordService.add(form)

        return ResponseEntity.created(
            URI.create("/api/mileage/records/$added")
        ).body(
            mapOf("id" to added)
        )
    }

    @GetMapping
    fun readRecords(): ResponseEntity<RecordDto> {
        return ResponseEntity.ok(RecordDto(recordQueryService.getAll()))
    }
}
