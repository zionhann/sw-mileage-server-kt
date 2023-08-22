package edu.handong.cseemileage.mileage.record.dto

import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

class RecordForm(

    @field:Positive(message = ExceptionMessage.RECORD_SEMESTER_IS_EMPTY)
    val semesterId: Int,

    @field:NotBlank(message = ExceptionMessage.RECORD_STUDENT_ID_IS_EMPTY)
    val studentId: String,

    @field:Positive(message = ExceptionMessage.RECORD_INVALID_COUNTS)
    val counts: Int = 1,

    val description1: String? = null,

    val description2: String? = null
)