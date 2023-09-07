package edu.handong.cseemileage.mileage.category.dto

import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.NotBlank

class CategoryForm(
    // nullable = false, default (X)
    @field:NotBlank(message = ExceptionMessage.CATEGORY_TITLE_IS_EMPTY)
    val title: String?,

    // nullable = false, default (O)
    val orderIdx: Int?,
    val itemType: String?,
    val isMulti: Boolean?,
    val categoryMaxPoints: Float?,

    // nullable = true
    val description1: String?,
    val description2: String?

)
