package edu.handong.cseemileage.dto.mileage.semesterItem

import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class SemesterItemMultipleForm(
    val copyFrom: List<Int> = listOf(),

    @field:NotBlank(message = ExceptionMessage.SEMESTER_NAME_COPY_TO_NOT_FOUND)
    @field:Pattern(regexp = "^(\\d{4}-(01|02))$", message = ExceptionMessage.INVALID_SEMESTER_NAME)
    val copyTo: String?
)
