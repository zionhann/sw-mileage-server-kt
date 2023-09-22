package edu.handong.cseemileage.dto.mileage.category

import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.PositiveOrZero

class CategoryForm(
    // nullable = false, default (X)
    @field:NotBlank(message = ExceptionMessage.CATEGORY_TITLE_IS_EMPTY)
    val title: String?,

    // nullable = false, default (O)
    val orderIdx: Int?,
    @field:Pattern(regexp = "^[R]$", message = ExceptionMessage.CATEGORY_INVALID_ITEM_TYPE)
    val itemType: String?,
    val isMulti: Boolean?,
    @field:PositiveOrZero(message = ExceptionMessage.CATEGORY_MAX_POINTS_IS_NEGATIVE)
    val categoryMaxPoints: Float?,

    // nullable = true
    val description1: String?,
    val description2: String?

)
