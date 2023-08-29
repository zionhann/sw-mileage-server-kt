package edu.handong.cseemileage.mileage.semesterItem.dto

import com.sun.istack.NotNull
import javax.validation.constraints.Positive

data class SemesterItemForm(
    // nullable = false, default (X)
    @field:Positive
    @field:NotNull
    val itemId: Int?,

    // nullable = false, default (O)
    val points: Float?,
    val itemMaxPoints: Float?,
    val categoryMaxPoints: Float?,

    // update 폼에서만 사용. create: PathVariable 사용
    val semesterName: String?
)
