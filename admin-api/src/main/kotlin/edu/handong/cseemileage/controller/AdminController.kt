package edu.handong.cseemileage.controller

import edu.handong.cseemileage.dto.account.admin.AdminDto
import edu.handong.cseemileage.dto.account.admin.AdminForm
import edu.handong.cseemileage.service.admin.AdminQueryService
import edu.handong.cseemileage.service.admin.AdminService
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
class AdminController(
    val adminService: AdminService,
    val adminQueryService: AdminQueryService
) {

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

    @PatchMapping("/{adminId}")
    fun modifyAdmin(
        @PathVariable adminId: Int,
        @RequestBody @Valid
        form: AdminForm
    ): ResponseEntity<Map<String, Int>> {
        val modifiedId = adminService.modifyAdmin(adminId, form)
        return ResponseEntity.ok(mapOf("id" to modifiedId))
    }

    @DeleteMapping("/{adminId}")
    fun deleteAdmin(
        @PathVariable
        adminId: Int
    ): ResponseEntity<Map<String, Int>> {
        val removedId = adminService.deleteAdmin(adminId)
        return ResponseEntity.ok(mapOf("id" to removedId))
    }
}
