package edu.handong.cseemileage.mileage.item.dto

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemDto

@JsonInclude(JsonInclude.Include.NON_NULL)
class ItemDto(
    var items: List<InfoV1>? = null
) {

    /**
     * Item과 Category 조회
     * */
    class InfoV1(
        val id: Int?,
        val category: CategoryDto.InfoV1?,
        val itemName: String?,
        val isPortfolio: Boolean?,
        val description1: String?,
        val description2: String?,
        val stuType: String?
    )

    /**
     * Item 단독 조회
     * */
    class InfoV2(
        val id: Int? = 0,
        val itemName: String? = "",
        val isPortfolio: Boolean? = false,
        val description1: String? = "",
        val description2: String? = "",
        val stuType: String? = ""
    )

    /**
     * Item과 Semester 조회
     * */
    class InfoV3(
        val id: Int?,
        val itemName: String?,
        val isPortfolio: Boolean?,
        val description1: String?,
        val description2: String?,
        val stuType: String?,
        val semesters: List<SemesterItemDto.InfoV3>
    )
}
