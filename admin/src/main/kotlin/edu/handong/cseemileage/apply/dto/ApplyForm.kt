package edu.handong.cseemileage.apply.dto

import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class ApplyForm(
    // nullable = false
    @field:Positive(message = ExceptionMessage.STUDENT_ID_IS_NOT_POSITIVE)
    @field:NotNull
    val studentId: Int?,

    // nullable = false, default(O)
    val semesterName: String?,
    val isApproved: Boolean?
)
