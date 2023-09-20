package edu.handong.cseemileage.mileage.mileageRecord.dto

import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

class MileageRecordForm(

    // nullable = false, default (x)
    @field:Positive(message = ExceptionMessage.RECORD_SEMESTER_IS_NOT_POSITIVE)
    val semesterItemId: Int?,
    val studentName: String?,
    @field:NotBlank(message = ExceptionMessage.STUDENT_SID_IS_EMPTY)
    val sid: String?,

    // nullable = true
    @field:Positive(message = ExceptionMessage.RECORD_INVALID_COUNTS)
    val counts: Float?,
    val extraPoints: Int?,
    val description1: String?,
    val description2: String?
)
