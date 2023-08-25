package edu.handong.cseemileage.student.dto

class StudentForm(
    val name: String?,
    val sid: String?,
    val mobile: String? = null,
    val email: String? = null,
    val isApproved: Boolean? = null,
    val department: String?,
    val major1: String?,
    val major2: String?,
    val year: Int?,
    val semesterCount: Int?
)
