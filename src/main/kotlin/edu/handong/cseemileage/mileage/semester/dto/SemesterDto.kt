package edu.handong.cseemileage.mileage.semester.dto

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.item.dto.ItemDto

@JsonInclude(JsonInclude.Include.NON_NULL)
class SemesterDto(
    val semesters: List<InfoV1>? = null
) {

    constructor() : this(listOf())

    class InfoV1(
        val item: ItemDto.InfoV2,
        val category: CategoryDto.InfoV1,
        val name: String,
        val weight: Float
    ) {
        constructor() : this(ItemDto.InfoV2(), CategoryDto.InfoV1(), "", 0f)
    }

    class InfoV2(
        val category: CategoryDto.InfoV3
    ) {
        constructor() : this(CategoryDto.InfoV3())
    }

    class InfoV3(
        val name: String,
        val weight: Float
    ) {
        constructor() : this("", 0f)
    }
}
