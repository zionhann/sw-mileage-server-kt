package edu.handong.cseemileage.mileage.subitem.dto

import edu.handong.cseemileage.mileage.category.dto.CategoryDto

class SubitemDto(
    val subitems: List<Info>
) {
    class Info(
        val id: Int?,
        val category: CategoryDto.Info?,
        val subitemName: String?,
        val weight: Float?,
        val isPortfolio: Int?,
        val maxPoint: Float?,
        val description1: String?,
        val description2: String?,
        val semester: String?,
        val stuType: String?
    )
}
