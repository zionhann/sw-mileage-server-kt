package edu.handong.cseemileage.controller

import edu.handong.cseemileage.dto.mileage.apply.ApplyDto
import edu.handong.cseemileage.dto.mileage.apply.ApplyForm
import edu.handong.cseemileage.service.ApplyQueryService
import edu.handong.cseemileage.service.ApplyService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/mileage/apply")
@CrossOrigin(origins = ["*"])
@Tag(name = "신청 API")
class ApplyController(
    val applyService: ApplyService,
    val applyQueryService: ApplyQueryService
) {
    @Operation(summary = "신청 기록 생성", description = "학생 시스템에서 마일리지 장학금을 신청합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "마일리지 장학금 신청 성공"),
            ApiResponse(responseCode = "400", description = "studentId 누락, studentId 0 이하, 학기 패턴 검증 실패 등의 이유로 마일리지 장학금 신청 실패"),
            ApiResponse(responseCode = "500", description = "해당 학생이 존재하지 않음, 해당 학기에 이미 신청함 등의 이유로 마일리지 장학금 신청 실패")
        ]
    )
    @PostMapping
    fun createApply(
        @RequestBody @Valid
        form: ApplyForm
    ): ResponseEntity<Map<String, Int>> {
        val savedId = applyService.saveApply(form)
        return ResponseEntity.created(
            URI.create("/api/mileage/apply/$savedId")
        ).body(mapOf("id" to savedId))
    }

    @Operation(summary = "마일리지 신청 기록 전체 조회", description = "마일리지 신청 정보를 전체 조회 합니다. list{신청 {학생} 리스트} 형식으로 반환됩니다")
    @ApiResponse(responseCode = "200", description = "마일리지 신청 기록 전체 조회 성공")
    @GetMapping
    fun getApplies(): ResponseEntity<ApplyDto> {
        val applies = applyQueryService.getApplies()
        return ResponseEntity.ok(
            ApplyDto(
                list = applies,
                count = applies.size,
                description = "신청 정보 전체 조회 결과"
            )
        )
    }

    @Operation(summary = "마일리지 신청 기록 조회 by applyId", description = "마일리지 신청 정보를 단건 조회합니다. list{신청 {학생} 리스트} 형식으로 반환됩니다")
    @Parameter(name = "applyId", description = "조회할 마일리지 신청 기록 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "마일리지 신청 기록 단건 조회 성공"),
            ApiResponse(responseCode = "500", description = "해당 신청 기록이 존재하지 않아 단건 조회 실패")
        ]
    )
    @GetMapping("/{applyId}")
    fun getApplyById(
        @PathVariable("applyId") applyId: Int
    ): ResponseEntity<ApplyDto> {
        val apply = applyQueryService.getApply(applyId)
        return ResponseEntity.ok(
            ApplyDto(
                data = apply,
                description = "신청 정보 단건 조회 by id (applyId = $applyId)"
            )
        )
    }

    @Operation(summary = "마일리지 신청 기록 조회 by semesterName", description = "특정 학기의 마일리지 신청 정보를 조회합니다. list{신청 {학생} 리스트} 형식으로 반환됩니다")
    @Parameter(name = "semesterName", description = "조회할 마일리지 신청 기록 semester", required = true, example = "2023-02")
    @ApiResponse(responseCode = "200", description = "마일리지 신청 기록 조회 성공 (by semesterName)")
    @GetMapping("/semester/{semesterName}")
    fun getApplyBySemesterName(
        @PathVariable("semesterName") semesterName: String
    ): ResponseEntity<ApplyDto> {
        val appliesBySemester = applyQueryService.getAppliesBySemesterName(semesterName)
        return ResponseEntity.ok(
            ApplyDto(
                list = appliesBySemester,
                count = appliesBySemester.size,
                description = "신청 정보 조회 by semester (semester = $semesterName)"
            )
        )
    }

    @Operation(summary = "마일리지 신청 기록 조회 by sid", description = "특정 학생의 마일리지 신청 정보를 조회합니다. list{신청 {학생} 리스트} 형식으로 반환됩니다")
    @Parameter(name = "sid", description = "학생 학번", required = true, example = "22000630")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "마일리지 신청 기록 조회 성공 (by sid)"),
            ApiResponse(responseCode = "500", description = "해당 학생이 존재하지 않아 마일리지 신청 기록 조회 실패")
        ]
    )
    @GetMapping("/student/{sid}")
    fun getApplyBySid(
        @PathVariable("sid") sid: String
    ): ResponseEntity<ApplyDto> {
        val appliesBySid = applyQueryService.getAppliesBySid(sid)
        return ResponseEntity.ok(
            ApplyDto(
                list = appliesBySid,
                count = appliesBySid.size,
                description = "신청 정보 조회 by sid (sid = $sid)"
            )
        )
    }

    @Operation(summary = "마일리지 장학금 신청 정보 수정", description = "학생 시스템에서 마일리지 장학금 신청 정보를 수정합니다. 학생 sid는 필수 요청이며, 나머지 필드는 수정을 원하는 경우 요청합니다.")
    @Parameter(name = "applyId", description = "신청 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "신청 기록 수정 성공"),
            ApiResponse(responseCode = "400", description = "sid 누락, sid 0 이하, 학기 패턴 검증 실패 등의 이유로 신청 정보 수정 실패"),
            ApiResponse(responseCode = "500", description = "해당 학생이 존재하지 않음, 해당 학기에 이미 신청함, 해당 신청 정보가 존재하지 않음 등의 이유로 마일리지 장학금 신청 실패")
        ]
    )
    @PatchMapping("/{applyId}")
    fun modifyApply(
        @PathVariable applyId: Int,
        @RequestBody @Valid
        form: ApplyForm
    ): ResponseEntity<Map<String, Int>> {
        val modifiedId = applyService.modifyApply(applyId, form)
        return ResponseEntity.ok(mapOf("id" to modifiedId))
    }

    // TODO: setting 기능 개발하면서 마일리지 신청과 취소를 제한해야함.(마일리지 장학 선청 중일 때 변경하지 못하도록)
    @Operation(summary = "마일리지 장학금 신청 취소", description = "학생 시스템에서 마일리지 장학금 신청을 취소합니다.")
    @Parameter(name = "applyId", description = "신청 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "마일리지 장학금 신청 취소"),
            ApiResponse(responseCode = "500", description = "applyId에 해당하는 신청 정보가 존재하지 않아 취소 실패")
        ]
    )
    @DeleteMapping("/{applyId}")
    fun deleteApply(
        @PathVariable
        applyId: Int
    ): ResponseEntity<Map<String, Int>> {
        val removedId = applyService.deleteApply(applyId)
        return ResponseEntity.ok(mapOf("id" to removedId))
    }
}
