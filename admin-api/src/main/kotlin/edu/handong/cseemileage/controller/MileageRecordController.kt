package edu.handong.cseemileage.controller

import edu.handong.cseemileage.dto.mileage.record.MileageRecordDto
import edu.handong.cseemileage.dto.mileage.record.MileageRecordForm
import edu.handong.cseemileage.service.record.MileageRecordQueryService
import edu.handong.cseemileage.service.record.MileageRecordService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "마일리지 기록 API")
@SecurityRequirement(name = "Bearer Authentication")
class MileageRecordController(
    private val mileageRecordService: MileageRecordService,
    private val mileageRecordQueryService: MileageRecordQueryService
) {

    @Operation(summary = "마일리지 기록 생성", description = "관리자 사이트에서 학기별 항목에 마일리지 기록을 추가합니다. 즉, 학생에게 마일리지를 적립합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "마일리지 기록 생성 성공"),
            ApiResponse(responseCode = "400", description = "semesterItemId 누락, 학생 이름 누락, sid 누락, sid 패턴 검증 실패, counts 0 이하, extraPoints 음수 등의 이유로 마일리지 기록 생성 실패"),
            ApiResponse(responseCode = "500", description = "존재하지 않는 학기별 항목을 참조하는 경우 마일리지 기록 생성 실패")
        ]
    )
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

    @Operation(summary = "마일리지 기록 전체 조회", description = "관리자 사이트에서 마일리지 기록을 전체 조회합니다. {마일리지 기록 {학기별 항목 {항목}, 카테고리} 리스트} 형식으로 반환합니다.")
    @ApiResponse(responseCode = "200", description = "마일리지 기록 전체 조회 성공")
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

    @Operation(summary = "마일리지 기록 조회 by semesterItemId", description = "관리자 사이트에서 학기별 항목에 등록된 마일리지 기록을 조회합니다.")
    @Parameter(name = "semesterItemId", description = "조회할 학기별 항목 PK", required = true, example = "1")
    @ApiResponse(responseCode = "200", description = "마일리지 기록 조회 성공 (by semesterItemId)")
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

    @Operation(summary = "(삭제불가원인 조회 용)마일리지 기록 조회 by sid", description = "학생 삭제 시도 시 해당 학생에게 등록된 마일리지 기록을 조회합니다. {마일리지 기록 {item, semesterItem} 리스트} 형식으로 반환합니다.")
    @Parameter(name = "sid", description = "삭제 시도한 학생 학번", required = true, example = "22000630")
    @ApiResponse(responseCode = "200", description = "마일리지 기록 조회 성공 (by sid)")
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

    @Operation(summary = "(삭제불가원인 조회 용)마일리지 기록 조회 by semesterItemId", description = "학기별 항목 삭제 시 해당 항목에 등록된 마일리지 기록을 조회합니다. {마일리지 기록 {item, semesterItem} 리스트} 형식으로 반환합니다.")
    @Parameter(name = "semesterItemId", description = "삭제 시도한 학기별 항목 PK", required = true, example = "1")
    @ApiResponse(responseCode = "200", description = "마일리지 기록 조회 성공 (by sid)")
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

    @Operation(summary = "마일리지 기록 수정", description = "관리자 사이트에서 학기별 항목에 등록된 마일리지 기록을 수정합니다.")
    @Parameter(name = "mileageRecordId", description = "마일리지 기록 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "마일리지 기록 수정 성공"),
            ApiResponse(responseCode = "400", description = "semesterItemId 누락, 학생 이름 누락, sid 누락, sid 패턴 검증 실패, counts 0 이하, extraPoints 음수 등의 이유로 마일리지 기록 생성 실패"),
            ApiResponse(responseCode = "500", description = "존재하지 않는 마일리지 기록인 경우 수정 실패")
        ]
    )
    @PatchMapping("/{mileageRecordId}")
    fun modifyMileageRecord(
        @PathVariable mileageRecordId: Int,
        @RequestBody @Valid
        form: MileageRecordForm
    ): ResponseEntity<Map<String, Int>> {
        val modifiedId = mileageRecordService.modifyMileageRecord(mileageRecordId, form)
        return ResponseEntity.ok(mapOf("id" to modifiedId))
    }

    @Operation(summary = "마일리지 기록 삭제", description = "마일리지 기록을 삭제합니다.")
    @Parameter(name = "mileageRecordId", description = "마일리지 기록 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "마일리지 기록 삭제 성공"),
            ApiResponse(responseCode = "500", description = "mileageRecordId에 해당하는 마일리지 기록이 존재하지 않아서 삭제 실패")
        ]
    )
    @DeleteMapping("/{mileageRecordId}")
    fun deleteItem(
        @PathVariable
        mileageRecordId: Int
    ): ResponseEntity<Map<String, Int>> {
        val removedId = mileageRecordService.deleteMileageRecord(mileageRecordId)
        return ResponseEntity.ok(mapOf("id" to removedId))
    }
}
