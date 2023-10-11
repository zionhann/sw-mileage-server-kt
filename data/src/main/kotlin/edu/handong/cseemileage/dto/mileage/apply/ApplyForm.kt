package edu.handong.cseemileage.dto.mileage.apply

import edu.handong.cseemileage.exception.ExceptionMessage
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

data class ApplyForm(
    // nullable = false
    @Schema(description = "학생 PK", example = "1")
    @field:Positive(message = ExceptionMessage.STUDENT_ID_IS_NOT_POSITIVE)
    @field:NotNull
    val studentId: Int?,

    // nullable = false, default(O)
    @Schema(description = "학기 이름", example = "2023-02")
    @field:Pattern(regexp = "^(\\d{4}-(01|02))$", message = ExceptionMessage.INVALID_SEMESTER_NAME)
    val semesterName: String?,
    val isApproved: Boolean?
)
