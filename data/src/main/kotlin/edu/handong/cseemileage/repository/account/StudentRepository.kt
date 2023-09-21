package edu.handong.cseemileage.repository.account

import edu.handong.cseemileage.domain.acount.Student
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface StudentRepository : JpaRepository<Student, Int> {

    fun findBySid(sid: String): Optional<Student>
}
