package edu.handong.cseemileage.admin.service

import edu.handong.cseemileage.admin.domain.Admin
import edu.handong.cseemileage.admin.dto.AdminDto
import edu.handong.cseemileage.admin.exception.AdminNotFoundException
import edu.handong.cseemileage.admin.repository.AdminRepository
import org.springframework.stereotype.Service

@Service
class AdminQueryService(
    val repository: AdminRepository
) {
    fun getAdmins(): List<AdminDto.Info> {
        val admins = repository.findAll()
        return admins.map { admin ->
            createAdminDto(admin)
        }
    }

    fun getAdmin(adminId: Int): AdminDto.Info {
        return createAdminDto(
            repository.findById(adminId)
                .orElseThrow { AdminNotFoundException() }
        )
    }

    private fun createAdminDto(admin: Admin): AdminDto.Info {
        return AdminDto.Info(
            id = admin.id,
            name = admin.name,
            email = admin.email,
            level = admin.level,
            loginCount = admin.loginCount,
            lastLoginDate = admin.lastLoginDate,
            modDate = admin.modDate
        )
    }
}
