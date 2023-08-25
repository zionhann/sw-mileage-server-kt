package edu.handong.cseemileage.mileage.semesterItem.dto

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.item.dto.ItemDto

@JsonInclude(JsonInclude.Include.NON_NULL)
class SemesterItemDto(
    val semesterItems: List<InfoV1>? = null
) {
    class InfoV1(
        val item: ItemDto.InfoV2,
        val category: CategoryDto.InfoV1,
        val semesterName: String,
        val weight: Float
    )

    class InfoV2(
        val category: CategoryDto.InfoV3
    )

    class InfoV3(
        val semesterName: String,
        val weight: Float
    )
}
