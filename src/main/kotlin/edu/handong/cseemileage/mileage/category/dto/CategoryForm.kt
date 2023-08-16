package edu.handong.cseemileage.mileage.category.dto

import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

class CategoryForm(
    @field:NotBlank(message = ExceptionMessage.CATEGORY_TITLE_IS_EMPTY)
    val title: String?,

    val description: String?,

    @field:PositiveOrZero(message = ExceptionMessage.CATEGORY_INVALID_POINTS)
    @field:NotNull(message = ExceptionMessage.CATEGORY_POINTS_IS_EMPTY)
    val maxPoints: Int?
)
