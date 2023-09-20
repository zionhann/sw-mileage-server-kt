package edu.handong.cseemileage.admin.repository

import edu.handong.cseemileage.admin.domain.Admin
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AdminRepository : JpaRepository<Admin, Int> {
    fun findByEmail(email: String): Optional<Admin>
}
