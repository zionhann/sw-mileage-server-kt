package edu.handong.cseemileage.mileage.item.dto

import com.sun.istack.NotNull
import javax.validation.constraints.Positive

data class ItemForm(
    @field:Positive
    val categoryId: Int?,
    @field:NotNull
    val itemName: String?,
    val description1: String?,
    val description2: String?,
    @field:NotNull
    val stuType: String?,
    @field:NotNull
    val flags: Flag
) {
    class Flag(
        @field:NotNull
        val isVisible: Boolean?,
        @field:NotNull
        val isStudentVisible: Boolean?,
        @field:NotNull
        val isStudentEditable: Boolean?,
        @field:NotNull
        val isMultiple: Boolean?,
        @field:NotNull
        val isPortfolio: Boolean?
    )
}
