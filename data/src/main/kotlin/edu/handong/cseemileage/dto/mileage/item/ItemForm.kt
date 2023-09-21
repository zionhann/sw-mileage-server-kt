package edu.handong.cseemileage.dto.mileage.item

import com.sun.istack.NotNull
import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

data class ItemForm(
    // nullable = false, default (X)
    @field:Positive(message = ExceptionMessage.ITEM_CATEGORY_ID_NOT_POSITIVE)
    @field:NotNull
    val categoryId: Int?,
    @field:NotBlank(message = ExceptionMessage.ITEM_NAME_IS_EMPTY)
    val itemName: String?,

    // nullable = true
    val description1: String?,
    val description2: String?,
    val stuType: String?,

    // nullable = false, default (O)
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
