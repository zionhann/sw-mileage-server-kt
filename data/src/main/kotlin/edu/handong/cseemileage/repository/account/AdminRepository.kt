package edu.handong.cseemileage.repository.account

import edu.handong.cseemileage.domain.acount.Admin
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AdminRepository : JpaRepository<Admin, Int> {
    fun findByAid(aid: String): Optional<Admin>
}
