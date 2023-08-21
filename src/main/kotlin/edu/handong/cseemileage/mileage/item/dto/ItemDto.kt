package edu.handong.cseemileage.mileage.item.dto

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.semester.dto.SemesterDto

@JsonInclude(JsonInclude.Include.NON_NULL)
class ItemDto(
    var items: List<InfoV1>? = null
) {

    constructor() : this(listOf())

    /**
     * Item과 Category 조회
     * */
    class InfoV1(
        val id: Int?,
        val category: CategoryDto.InfoV1?,
        val itemName: String?,
        val isPortfolio: Int?,
        val description1: String?,
        val description2: String?,
        val stuType: String?
    ) {
        constructor() : this(0, CategoryDto.InfoV1(), "", 0, "", "", "")
    }

    /**
     * Item 단독 조회
     * */
    class InfoV2(
        val id: Int?,
        val itemName: String?,
        val isPortfolio: Int?,
        val description1: String?,
        val description2: String?,
        val stuType: String?
    ) {
        constructor() : this(0, "", 0, "", "", "")
    }

    /**
     * Item과 Semester 조회
     * */
    class InfoV3(
        val id: Int?,
        val itemName: String?,
        val isPortfolio: Int?,
        val description1: String?,
        val description2: String?,
        val stuType: String?,
        val semesters: List<SemesterDto.InfoV3>
    ) {
        constructor() : this(0, "", 0, "", "", "", listOf())
    }
}
