package edu.handong.cseemileage.student.dto

class StudentForm(
    // nullable = true
    val name: String?,
    val sid: String?,
    val department: String?,
    val major1: String?,
    val major2: String?,
    val year: Int?,
    val semesterCount: Int?,

    // nullable = false, default (O)
    val isChecked: Boolean?

    // loginCount,lastLoginDate는 create, update 시 요청을 받지 않는 필드이므로 제외
)
