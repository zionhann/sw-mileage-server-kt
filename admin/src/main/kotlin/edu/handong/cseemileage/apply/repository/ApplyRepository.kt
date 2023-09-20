package edu.handong.cseemileage.apply.repository

import edu.handong.cseemileage.apply.domain.Apply
import edu.handong.cseemileage.student.domain.Student
import org.springframework.data.jpa.repository.JpaRepository

interface ApplyRepository : JpaRepository<Apply, Int> {
    fun findBySemesterNameAndStudent(semesterName: String, student: Student): Apply?
    fun findAllByStudent(student: Student): List<Apply>
    fun findAllBySemesterName(semesterName: String): List<Apply>
}
