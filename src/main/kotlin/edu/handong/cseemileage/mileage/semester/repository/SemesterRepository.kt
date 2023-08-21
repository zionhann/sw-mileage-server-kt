package edu.handong.cseemileage.mileage.semester.repository

import edu.handong.cseemileage.mileage.semester.domain.Semester
import org.springframework.data.jpa.repository.JpaRepository

interface SemesterRepository : JpaRepository<Semester, Int> {
    fun findTopByOrderByIdDesc(): Semester?
    fun findAllByName(name: String): List<Semester>
}
