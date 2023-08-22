package edu.handong.cseemileage.student.repository

import edu.handong.cseemileage.student.domain.Student
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface StudentRepository : JpaRepository<Student, Int> {

    fun findBySid(sid: String): Optional<Student>
}
