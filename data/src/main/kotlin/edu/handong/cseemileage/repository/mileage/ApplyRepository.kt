package edu.handong.cseemileage.repository.mileage

import edu.handong.cseemileage.domain.acount.Student
import edu.handong.cseemileage.domain.mileage.Apply
import org.springframework.data.jpa.repository.JpaRepository

interface ApplyRepository : JpaRepository<Apply, Int> {
    fun findBySemesterNameAndStudent(semesterName: String, student: Student): Apply?
    fun findAllByStudent(student: Student): List<Apply>
    fun findAllBySemesterName(semesterName: String): List<Apply>
}
