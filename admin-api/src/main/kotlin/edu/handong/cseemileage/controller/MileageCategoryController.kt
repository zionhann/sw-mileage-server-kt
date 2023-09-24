package edu.handong.cseemileage.controller

import edu.handong.cseemileage.dto.mileage.category.CategoryDto
import edu.handong.cseemileage.dto.mileage.category.CategoryForm
import edu.handong.cseemileage.exception.mileage.category.CategoryCannotDeleteException
import edu.handong.cseemileage.exception.mileage.semesterItem.SemesterPatternException
import edu.handong.cseemileage.service.category.CategoryQueryService
import edu.handong.cseemileage.service.category.CategoryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/mileage/categories")
@CrossOrigin(origins = ["*"])
@Tag(name = "카테고리 API")
class MileageCategoryController(
    val categoryService: CategoryService,
    val categoryQueryService: CategoryQueryService
) {

    @Operation(summary = "마일리지 카테고리 생성", description = "관리자 사이트에서 마일리지 카테고리를 생성합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "카테고리 생성 성공"),
            ApiResponse(responseCode = "400", description = "카테고리 title 누락, itemType 형식 오류, categoryMaxPoints 범위 초과 등 카테고리 생성 실패"),
            ApiResponse(responseCode = "500", description = "카테고리 name 중복으로 인해 카테고리 생성 실패")
        ]
    )
    @PostMapping
    fun createCategory(
        @RequestBody @Valid
        form: CategoryForm
    ): ResponseEntity<Map<String, Int>> {
        val savedId = categoryService.saveCategory(form)
        return ResponseEntity.created(
            URI.create("/api/mileage/categories/$savedId")
        ).body(mapOf("id" to savedId))
    }

    @Operation(summary = "카테고리 목록 전체 조회", description = "현재 등록되어 있는 카테고리 목록을 전체 조회합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 목록 전체 조회 성공")
    @GetMapping
    fun getCategories(): ResponseEntity<CategoryDto> {
        val categories = categoryQueryService.getCategories()
        return ResponseEntity.ok(
            CategoryDto(
                list = categories,
                count = categories.size,
                description = "카테고리 목록 조회 결과"
            )
        )
    }

    @Operation(summary = "카테고리 단건 조회 by categoryId(카테고리 PK)", description = "하나의 카테고리를 선택해 해당 카테고리의 상세 정보를 조회합니다.")
    @Parameter(name = "categoryId", description = "카테고리 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "카테고리 단건 조회 성공"),
            ApiResponse(responseCode = "500", description = "카테고리 단건 조회 실패 - categoryId 해당하는 카테고리가 존재하지 않음")
        ]
    )
    @GetMapping("{categoryId}")
    fun getOneCategory(
        @PathVariable categoryId: Int
    ): ResponseEntity<CategoryDto> {
        val category = categoryQueryService.getCategoryById(categoryId)
        return ResponseEntity.ok(
            CategoryDto(
                data = category,
                description = "카테고리 단건 조회 by categoryId ( = $categoryId )"
            )
        )
    }

    @Operation(summary = "카테고리, 항목, 학기별 항목 join 조회 by semester(ex. 2023-02)", description = "semester에 등록된 학기별 항목, 항목, 카테고리 정보를 조인해 조회합니다.")
    @Parameter(name = "semester", description = "학기별 항목 도메인의 semesterName 필드", required = true, example = "2023-02")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "학기별 항목, 항목, 카테고리 조회 성공 by semester"),
            ApiResponse(responseCode = "500", description = "학기 형식 오류")
        ]
    )
    @GetMapping("/v2")
    fun getItems(
        @RequestParam("semester") semester: String
    ): ResponseEntity<CategoryDto> {
        if (!semester.matches("^(\\d{4}-(01|02))$".toRegex())) {
            throw SemesterPatternException()
        }
        val list = categoryQueryService.getCategoryWithItemAndSemester(semester)
        return ResponseEntity.ok(
            CategoryDto(
                list = list,
                count = list.size,
                description = "학기별로 카테고리, 항목, 학기별 항목 정보 조회한 결과 (semester = $semester)"
            )
        )
    }

    @Operation(summary = "카테고리 수정 by id(카테고리 PK)", description = "하나의 카테고리 정보를 수정합니다. title 정보는 필수로 요청해야 하며 나머지 정보는 수정을 원하는 경우 요청합니다.")
    @Parameter(name = "id", description = "카테고리 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "카테고리 수정 성공"),
            ApiResponse(responseCode = "400", description = "카테고리 title 누락, itemType 형식 오류, categoryMaxPoints 범위 초과 등 카테고리 수정 실패"),
            ApiResponse(responseCode = "500", description = "카테고리 name 중복, id에 해당하는 카테고리가 존재하지 않음")
        ]
    )
    @PatchMapping("/{id}")
    fun updateCategory(
        @RequestBody @Valid
        form: CategoryForm,
        @PathVariable id: Int
    ): ResponseEntity<Map<String, Int>> {
        val updatedId = categoryService.update(id, form)
        return ResponseEntity.ok(mapOf("id" to updatedId))
    }

    @Operation(summary = "카테고리 삭제 by id(카테고리 PK)", description = "카테고리를 삭제 합니다. 해당 카테고리를 참조하는 하위 항목이 있을 경우 삭제 불가능합니다.")
    @Parameter(name = "id", description = "카테고리 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "카테고리 삭제 성공"),
            ApiResponse(responseCode = "500", description = "id에 해당하는 카테고리가 존재하지 않음, 카테고리를 참조하는 하위 항목이 존재함 등의 이유로 삭제 실패")
        ]
    )
    @DeleteMapping("/{id}")
    fun removeCategory(
        @PathVariable id: Int
    ): ResponseEntity<Map<String, Int>> {
        try {
            val removedId = categoryService.remove(id)
            return ResponseEntity.ok(mapOf("id" to removedId))
        } catch (e: DataIntegrityViolationException) {
            throw CategoryCannotDeleteException()
        }
    }
}
