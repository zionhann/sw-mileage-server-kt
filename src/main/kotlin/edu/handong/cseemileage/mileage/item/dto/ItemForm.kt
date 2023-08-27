package edu.handong.cseemileage.mileage.item.dto

import com.sun.istack.NotNull
import javax.validation.constraints.Positive

data class ItemForm(
    // nullable = false, default (X)
    @field:Positive
    @field:NotNull
    val categoryId: Int?,
    @field:NotNull
    val itemName: String?,

    // nullable = true
    val description1: String?,
    val description2: String?,
    val stuType: String?,

    // nullable = false, default (O)
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
