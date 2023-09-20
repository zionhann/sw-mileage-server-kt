package edu.handong.cseemileage.apply.dto

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.student.dto.StudentDto
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApplyDto(
    val description: String,
    val count: Int? = null,
    val list: List<ApplyDto.Info>? = null,
    val data: ApplyDto.Info? = null
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class Info(
        val id: Int? = null,
        val semesterName: String? = null,
        val isApproved: Boolean? = null,
        val student: StudentDto.Info? = null,
        val modDate: LocalDateTime? = null
    )
}
