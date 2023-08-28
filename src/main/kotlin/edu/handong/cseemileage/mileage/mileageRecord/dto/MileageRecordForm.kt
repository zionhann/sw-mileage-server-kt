package edu.handong.cseemileage.mileage.mileageRecord.dto

import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.Positive

class MileageRecordForm(

    // nullable = false, default (x)
    @field:Positive(message = ExceptionMessage.RECORD_SEMESTER_IS_EMPTY)
    val semesterItemId: Int?,
    @field:Positive(message = ExceptionMessage.RECORD_STUDENT_ID_IS_EMPTY)
    val studentId: Int?,

    // nullable = true
    @field:Positive(message = ExceptionMessage.RECORD_INVALID_COUNTS)
    val counts: Float?,
    val points: Int?,
    val extraPoints: Int?,
    val description1: String?,
    val description2: String?
)
