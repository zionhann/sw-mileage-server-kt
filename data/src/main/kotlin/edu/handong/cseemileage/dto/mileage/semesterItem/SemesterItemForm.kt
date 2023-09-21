package edu.handong.cseemileage.dto.mileage.semesterItem

import com.sun.istack.NotNull
import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

data class SemesterItemForm(
    // nullable = false, default (X)
    @field:Positive(message = ExceptionMessage.SEMESTER_ITEM_ID_IS_NOT_POSITIVE)
    @field:NotNull
    val itemId: Int?,

    // nullable = false, default (O)
    @field:PositiveOrZero(message = ExceptionMessage.SEMESTER_ITEM_POINTS_IS_NEGATIVE)
    val points: Float?,
    @field:PositiveOrZero(message = ExceptionMessage.ITEM_MAX_POINTS_IS_NEGATIVE)
    val itemMaxPoints: Float?,

    // update 폼에서만 사용. create: PathVariable 사용
    @field:Pattern(regexp = "^(\\d{4}-(01|02))$", message = ExceptionMessage.SEMESTER_ITEM_INVALID_SEMESTER_NAME)
    val semesterName: String?
)
