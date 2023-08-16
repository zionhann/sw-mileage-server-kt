package edu.handong.cseemileage.mileage.category.dto

class CategoryDto(
    val categories: List<Info>
) {
    class Info(
        val id: Int?,
        val title: String?,
        val maxPoints: Int?
    )
}
