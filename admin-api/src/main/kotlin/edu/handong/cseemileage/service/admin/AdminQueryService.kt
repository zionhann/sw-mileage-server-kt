package edu.handong.cseemileage.service.admin

import edu.handong.cseemileage.domain.acount.Admin
import edu.handong.cseemileage.dto.account.admin.AdminDto
import edu.handong.cseemileage.exception.account.admin.AdminNotFoundException
import edu.handong.cseemileage.repository.account.AdminRepository
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
