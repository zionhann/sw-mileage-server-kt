package edu.handong.cseemileage.dto.mileage.item

import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

data class ItemForm(
    // nullable = false, default (X)
    @field:Positive(message = ExceptionMessage.ITEM_CATEGORY_ID_NOT_POSITIVE)
    val categoryId: Int?,
    @field:NotBlank(message = ExceptionMessage.ITEM_NAME_IS_EMPTY)
    val itemName: String?,

    // nullable = true
    val description1: String?,
    val description2: String?,
    @field:Pattern(regexp = "^(|C|F|CF)$", message = ExceptionMessage.ITEM_INVALID_STU_TYPE)
    val stuType: String?,

    // nullable = false, default (O)
    @field:PositiveOrZero(message = ExceptionMessage.ITEM_MAX_POINTS_IS_NEGATIVE)
    val itemMaxPoints: Float?,
    val flags: Flag?
) {
    class Flag(
        val isVisible: Boolean?,
        val isStudentVisible: Boolean?,
        val isStudentEditable: Boolean?,
        val isMultiple: Boolean?,
        val isPortfolio: Boolean?
    )
}
