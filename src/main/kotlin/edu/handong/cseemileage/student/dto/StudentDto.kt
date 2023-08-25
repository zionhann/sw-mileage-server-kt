package edu.handong.cseemileage.student.dto

import edu.handong.cseemileage.student.domain.Student

class StudentDto(
    val students: List<Info>
) {
    class Info(student: Student) {
        val name: String = student.name!!
        val sid: String = student.sid!!
        val year: Int = student.year!!
        val semesterCount: Int = student.semesterCount!!
        val mobile: String = "" // TODO: Not specified yet
        val email: String = "" // TODO: Not specified yet
        val department: String = student.department!!
        val major1: String = student.major1!!
        val major2: String = student.major2!!
        val loginCount: Int = student.loginCount
        val lastLoginDate: String = student.lastLoginDate.toString()
        val regDate: String = student.regDate.toString()
        val isApproved: Boolean = true // TODO: Not specified yet
    }
}
