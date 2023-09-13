package edu.handong.cseemileage.student.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
class StudentDto(
    val description: String,
    val count: Int? = null,
    val list: List<Info>? = null,
    val data: Info? = null
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class Info(
        val id: Int? = null,
        val name: String? = null,
        val sid: String? = null,
        val department: String? = null,
        val major1: String? = null,
        val major2: String? = null,
        val year: Int? = null,
        val semesterCount: Int? = null,
        val loginCount: Int? = null,
        val lastLoginDate: LocalDateTime? = null,
        val isChecked: Boolean? = null
    )
}
