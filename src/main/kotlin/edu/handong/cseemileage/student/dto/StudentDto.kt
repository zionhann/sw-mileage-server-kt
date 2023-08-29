package edu.handong.cseemileage.student.dto

import java.time.LocalDateTime

class StudentDto(
    val students: List<Info>
) {
    class Info(
        val id: Int? = 0,
        val name: String? = null,
        val sid: String? = null,
        val department: String? = null,
        val major1: String? = null,
        val major2: String? = null,
        val year: Int? = 0,
        val semesterCount: Int? = 0,
        val loginCount: Int? = 0,
        val lastLoginDate: LocalDateTime = LocalDateTime.now(),
        val isChecked: Boolean = true
    )
}
