package edu.handong.cseemileage.controller

import edu.handong.cseemileage.dto.account.admin.AdminDto
import edu.handong.cseemileage.dto.account.admin.AdminForm
import edu.handong.cseemileage.service.admin.AdminQueryService
import edu.handong.cseemileage.service.admin.AdminService
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
@RequestMapping("/api/mileage/admins")
@CrossOrigin(origins = ["*"])
@Tag(name = "관리자 API")
class AdminController(
    val adminService: AdminService,
    val adminQueryService: AdminQueryService
) {

    // TODO: 관리자 회원가입 API와 구글 로그인 연동, jwt 적용 상의하기
    @Operation(summary = "관리자 회원가입", description = "관리자 사이트에서 최초 로그인하는 사용자에 대해 관리자 계정을 생성합니다. 관리자의 email은 중복 불가합니다. ")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "관리자 회원가입 성공"),
            ApiResponse(responseCode = "400", description = "email/name 누락, email 형식 오류, level 범위 초과 등 관리자 회원가입 실패"),
            ApiResponse(responseCode = "500", description = "관리자 email 중복으로 인해 회원가입 실패")
        ]
    )
    @PostMapping
    fun createAdmin(
        @RequestBody @Valid
        form: AdminForm
    ): ResponseEntity<Map<String, Int>> {
        val savedId = adminService.saveAdmin(form)
        return ResponseEntity.created(
            URI.create("/api/mileage/admins/$savedId")
        ).body(mapOf("id" to savedId))
    }

    @Operation(summary = "관리자 목록 전체 조회", description = "현재 가입되어 있는 관리자의 목록을 전체 조회합니다.")
    @ApiResponse(responseCode = "200", description = "관리자 목록 전체 조회 성공")
    @GetMapping
    fun getAdmins(): ResponseEntity<AdminDto> {
        val admins = adminQueryService.getAdmins()
        return ResponseEntity.ok(
            AdminDto(
                list = admins,
                count = admins.size,
                description = "관리자 목록 전체 조회 결과"
            )
        )
    }

    @Operation(summary = "관리자 단건 조회 by adminId(관리자 PK)", description = "한 명의 관리자를 선택해 해당 관리자의 상세 정보를 조회합니다.")
    @Parameter(name = "adminId", description = "관리자 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "관리자 단건 조회 성공"),
            ApiResponse(responseCode = "500", description = "관리자 단건 조회 실패 - adminId에 해당하는 관리자가 존재하지 않음")
        ]
    )
    @GetMapping("/{adminId}")
    fun getAdmin(
        @PathVariable adminId: Int
    ): ResponseEntity<AdminDto> {
        val admin = adminQueryService.getAdmin(adminId)
        return ResponseEntity.ok(
            AdminDto(
                data = admin,
                description = "관리자 단건 조회 결과 (adminId = $adminId)"
            )
        )
    }

    @Operation(summary = "관리자 수정 by adminId(관리자 PK)", description = "한 명의 관리자 정보를 수정합니다. name, email 정보는 필수로 요청해야 하지만 level은 필수 요청 아닙니다.")
    @Parameter(name = "adminId", description = "관리자 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "관리자 수정 성공"),
            ApiResponse(responseCode = "400", description = "name/email 누락, email 형식 오류, level 범위 초과 등 관리자 수정 실패"),
            ApiResponse(responseCode = "500", description = "관리자 email 중복으로 인해 관리자 수정 실패")
        ]
    )
    @PatchMapping("/{adminId}")
    fun modifyAdmin(
        @PathVariable adminId: Int,
        @RequestBody @Valid
        form: AdminForm
    ): ResponseEntity<Map<String, Int>> {
        val modifiedId = adminService.modifyAdmin(adminId, form)
        return ResponseEntity.ok(mapOf("id" to modifiedId))
    }

    @Operation(summary = "관리자 탈퇴 by adminId(관리자 PK)", description = "관리자 계정을 삭제(탈퇴) 합니다.")
    @Parameter(name = "adminId", description = "관리자 PK", required = true, example = "1")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "관리자 삭제 성공"),
            ApiResponse(responseCode = "500", description = "관리자 삭제 실패 - adminId에 해당하는 관리자가 존재하지 않음")
        ]
    )
    @DeleteMapping("/{adminId}")
    fun deleteAdmin(
        @PathVariable
        adminId: Int
    ): ResponseEntity<Map<String, Int>> {
        val removedId = adminService.deleteAdmin(adminId)
        return ResponseEntity.ok(mapOf("id" to removedId))
    }
}
