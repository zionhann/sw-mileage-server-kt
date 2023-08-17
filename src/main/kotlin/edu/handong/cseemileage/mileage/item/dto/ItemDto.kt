package edu.handong.cseemileage.mileage.item.dto

import edu.handong.cseemileage.mileage.category.dto.CategoryDto

class ItemDto(
    val items: List<Info>
) {
    class Info(
        val id: Int?,
        val category: CategoryDto.Info?,
        val itemName: String?,
        val isPortfolio: Int?,
        val description1: String?,
        val description2: String?,
        val semester: String?,
        val stuType: String?
    )
}
