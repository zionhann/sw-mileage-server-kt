package edu.handong.cseemileage.dto.account.student

import edu.handong.cseemileage.annotation.InclusiveRange
import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

class StudentForm(
    // nullable = true
    val name: String?,

    @field:NotBlank(message = ExceptionMessage.STUDENT_SID_IS_EMPTY)
    @field:Pattern(regexp = "^\\d{8}$", message = ExceptionMessage.STUDENT_INVALID_SID)
    val sid: String?,

    val department: String?,
    val major1: String?,
    val major2: String?,
    @field:InclusiveRange(min = 1, max = 4, field = "학년")
    val year: Int?,
    @field:InclusiveRange(min = 1, max = 8, field = "학기 수")
    val semesterCount: Int?,

    // nullable = false, default (O)
    val isChecked: Boolean?

    // loginCount,lastLoginDate는 create, update 시 요청을 받지 않는 필드이므로 제외
)
