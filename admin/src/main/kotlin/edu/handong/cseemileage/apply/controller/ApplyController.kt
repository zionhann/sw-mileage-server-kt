package edu.handong.cseemileage.apply.controller

import edu.handong.cseemileage.apply.dto.ApplyDto
import edu.handong.cseemileage.apply.dto.ApplyForm
import edu.handong.cseemileage.apply.service.ApplyQueryService
import edu.handong.cseemileage.apply.service.ApplyService
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
class ApplyController(
    val applyService: ApplyService,
    val applyQueryService: ApplyQueryService
) {
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

    @PatchMapping("/{applyId}")
    fun modifyApply(
        @PathVariable applyId: Int,
        @RequestBody @Valid
        form: ApplyForm
    ): ResponseEntity<Map<String, Int>> {
        val modifiedId = applyService.modifyApply(applyId, form)
        return ResponseEntity.ok(mapOf("id" to modifiedId))
    }

    @DeleteMapping("/{applyId}")
    fun deleteApply(
        @PathVariable
        applyId: Int
    ): ResponseEntity<Map<String, Int>> {
        val removedId = applyService.deleteApply(applyId)
        return ResponseEntity.ok(mapOf("id" to removedId))
    }
}
