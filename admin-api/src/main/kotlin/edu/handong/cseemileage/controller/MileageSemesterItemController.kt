package edu.handong.cseemileage.controller

import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemDto
import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemForm
import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemMultipleForm
import edu.handong.cseemileage.exception.mileage.semesterItem.SemesterItemCannotDeleteException
import edu.handong.cseemileage.service.semesterItem.SemesterItemQueryService
import edu.handong.cseemileage.service.semesterItem.SemesterItemService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "학기별 항목 API")
@SecurityRequirement(name = "Bearer Authentication")
class MileageSemesterItemController(
    val semesterItemService: SemesterItemService,
    val semesterItemQueryService: SemesterItemQueryService
) {

    companion object {
        private const val API_URI = "/api/mileage/semesters"
    }

    @Operation(summary = "학기별 항목 생성", description = "관리자 사이트에서 학기별 항목을 생성합니다.")
    @Parameter(name = "semesterName", description = "등록할 학기", required = true, example = "2023-02")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "학기별 항목 생성 성공"),
            ApiResponse(responseCode = "400", description = "itemId 누락, itemId 0이하, points 음수, itemMaxPoints 음수 등의 이유로 학기별 항목 생성 실패"),
            ApiResponse(responseCode = "500", description = "이미 해당 학기에 등록된 항목인 경우, 존재하지 않는 학기를 참조하는 경우 학기별 항목 생성 실패")
        ]
    )
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

    @Operation(summary = "학기별 항목 다중 생성", description = "관리자 사이트에서 학기별 항목을 다중 생성합니다.")
    @ApiResponse(responseCode = "201", description = "학기별 항목 다중 생성 성공")
    @PostMapping("/multiple")
    fun createSemesterMultiple(
        @RequestBody @Valid
        form: SemesterItemMultipleForm
    ): ResponseEntity<String> {
        val map = semesterItemService.saveSemesterItemMultipleBulkInsert(form)
        return ResponseEntity.created(
            URI.create("$API_URI/multiple")
        ).body(
            "학기별 항목 생성(multiple) 완료" +
                "\n복사 성공: ${map["복사 성공"]}" +
                "\n복사 실패 - 중복 항목: ${map["복사 실패 - 중복 항목"]}" +
                "\n복사 실패 - 존재하지 않는 항목: ${map["복사 실패 - 학기별 항목을 찾지 못함"]}"
        )
    }

    @Operation(summary = "학기별 항목 조회 by semesterName", description = "관리자 사이트에서 학기로 필터링해서 항목을 조회합니다. list{학기별 항목 {항목, 카테고리} 리스트} 형식으로 반환됩니다")
    @Parameter(name = "semesterName", description = "조회할 학기", required = true, example = "2023-02")
    @ApiResponse(responseCode = "200", description = "학기별 항목 조회 성공 (by semesterName)")
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

    @Operation(summary = "학기별 항목 (with 마일리지 기록) 조회", description = "관리자 사이트에서 학기별 항목 리스트를 조회합니다. 이때 각 학기별 항목에 등록된 마일리지 기록도 함께 조회합니다. list{학기별 항목 {항목, 카테고리, {마일리지 기록 리스트}} 리스트} 형식으로 반환됩니다")
    @Parameter(name = "semesterName", description = "조회할 학기", required = true, example = "2023-02")
    @ApiResponse(responseCode = "200", description = "학기별 항목 (with 마일리지 기록) 조회 성공 (by semesterName)")
    @GetMapping("/{semesterName}/items/records")
    fun getSemesterItemsAndRecords(
        @PathVariable semesterName: String
    ): ResponseEntity<SemesterItemDto> {
        val semesterItems = semesterItemQueryService.getSemesterItemsWithRecords(semesterName)
        return ResponseEntity.ok(
            SemesterItemDto(
                list = semesterItems,
                count = semesterItems.size,
                description = "학기별 항목 조회 join with 카테고리, 항목, 마일리지 기록"
            )
        )
    }

    @Operation(summary = "학기별 항목 (by itemId) 조회", description = "관리자 사이트에서 글로벌 항목이 어떤 학기에 등록되었는지 조회할 때 사용합니다.")
    @Parameter(name = "itemId", description = "항목 PK", required = true, example = "1")
    @ApiResponse(responseCode = "200", description = "학기별 항목 (by itemId) 조회 성공 (by itemId)")
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

    @Operation(summary = "항목 수정 by itemId(항목 PK)", description = "하나의 항 정보를 수정합니다. name 정보는 필수로 요청해야 하며 나머지 정보는 수정을 원하는 경우 요청합니다.")
    @Parameter(name = "itemId", description = "항목 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "학기별 항목 수정 성공"),
            ApiResponse(responseCode = "400", description = "itemId 누락, itemId 0이하, points 음수, itemMaxPoints 음수, semesterName 패턴 검증 실패 등의 이유로 학기별 항목 조회 실패"),
            ApiResponse(responseCode = "500", description = "semesterName 누락, itemId에 해당하는 항목 존재하지 않음, 해당하는 학기별 항목이 존재하지 않음, 중복 학기별 항목 등 이유로 학기별 항목 수정 실패")
        ]
    )
    @PatchMapping("/{semesterItemId}")
    fun modifyItem(
        @PathVariable semesterItemId: Int,
        @RequestBody @Valid
        form: SemesterItemForm
    ): ResponseEntity<Map<String, Int>> {
        val modifiedId = semesterItemService.modifySemesterItem(semesterItemId, form)
        return ResponseEntity.ok(mapOf("id" to modifiedId))
    }

    @Operation(summary = "학기별 항목 삭제 by semesterItemId(학기별 항목 PK)", description = "학기별 항목을 삭제 합니다. 해당 학기별 항목을 참조하는 마일리지 기록이 있을 경우 삭제 불가능합니다.")
    @Parameter(name = "semesterItemId", description = "학기별 항목 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "학기별 항목 삭제 성공"),
            ApiResponse(responseCode = "500", description = "semesterItemId에 해당하는 학기별 항목이 존재하지 않음, 학기별 항목을 참조하는 마일리지 기록이 존재함 등의 이유로 삭제 실패")
        ]
    )
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
