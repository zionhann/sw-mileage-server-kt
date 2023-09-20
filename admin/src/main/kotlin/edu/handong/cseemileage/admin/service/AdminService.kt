package edu.handong.cseemileage.admin.service

import edu.handong.cseemileage.admin.domain.Admin
import edu.handong.cseemileage.admin.dto.AdminForm
import edu.handong.cseemileage.admin.exception.AdminNotFoundException
import edu.handong.cseemileage.admin.exception.DuplicateAdminException
import edu.handong.cseemileage.admin.repository.AdminRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminService(
    val repository: AdminRepository
) {

    fun saveAdmin(form: AdminForm): Int {
        validateDuplicateAdmin(form.email!!, 0)
        val admin = Admin.createAdmin(form)
        repository.save(admin)
        return admin.id!!
    }

    private fun validateDuplicateAdmin(email: String, id: Int) {
        if (id > 0) {
            val findAdmin = repository.findById(id).orElseThrow() { AdminNotFoundException() }
            if (findAdmin.email == email) return
        }
        val findAdmin = repository.findByEmail(email)
        if (findAdmin.isPresent) throw DuplicateAdminException()
    }

    fun modifyAdmin(adminId: Int, form: AdminForm): Int {
        validateDuplicateAdmin(form.email!!, adminId)
        return repository
            .findById(adminId)
            .orElseThrow { AdminNotFoundException() }
            .update(form)
    }

    fun deleteAdmin(adminId: Int): Int {
        repository.deleteById(adminId)
        return adminId
    }
}
