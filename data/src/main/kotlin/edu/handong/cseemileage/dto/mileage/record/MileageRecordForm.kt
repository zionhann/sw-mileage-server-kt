package edu.handong.cseemileage.dto.mileage.record

import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

class MileageRecordForm(

    // nullable = false, default (x)
    @field:Positive(message = ExceptionMessage.RECORD_SEMESTER_IS_NOT_POSITIVE)
    val semesterItemId: Int?,
    val studentName: String?,
    @field:NotBlank(message = ExceptionMessage.STUDENT_SID_IS_EMPTY)
    @field:Pattern(regexp = "^\\d{8}$", message = ExceptionMessage.STUDENT_INVALID_SID)
    val sid: String?,

    // nullable = true
    @field:Positive(message = ExceptionMessage.RECORD_INVALID_COUNTS)
    val counts: Float?,
    @field:PositiveOrZero(message = ExceptionMessage.RECORD_INVALID_EXTRA_POINTS)
    val extraPoints: Int?,
    val description1: String?,
    val description2: String?
)
