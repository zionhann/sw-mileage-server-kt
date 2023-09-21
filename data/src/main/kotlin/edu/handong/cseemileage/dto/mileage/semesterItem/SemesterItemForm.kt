package edu.handong.cseemileage.dto.mileage.semesterItem

import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.Positive

data class SemesterItemForm(
    // nullable = false, default (X)
    @field:Positive(message = ExceptionMessage.SEMESTER_ITEM_ID_IS_NOT_POSITIVE)
    val itemId: Int?,

    // nullable = false, default (O)
    val points: Float?,
    val itemMaxPoints: Float?,

    // update 폼에서만 사용. create: PathVariable 사용
    val semesterName: String?
)
