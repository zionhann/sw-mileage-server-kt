package edu.handong.cseemileage.mileage.item.dto

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemDto

@JsonInclude(JsonInclude.Include.NON_NULL)
class ItemDto(
    var items: List<InfoV1>? = null,
    var deleteFailureReasons: List<deleteFailureInfo>? = null
) {

    /**
     * Item과 Category 조회
     * */
    class InfoV1(
        val id: Int = 0,
        val category: CategoryDto.InfoV1 = CategoryDto.InfoV1(),
        val name: String = "",
        val isPortfolio: Boolean = false,
        val description1: String? = null,
        val description2: String? = null,
        val stuType: String? = null,
        val isVisible: Boolean = true,
        val isStudentVisible: Boolean = true,
        val isStudentInput: Boolean = true,
        val isMulti: Boolean = true
    )

    /**
     * Item 단독 조회
     * */
    class InfoV2(
        val id: Int = 0,
        val name: String = "",
        val isPortfolio: Boolean = false,
        val description1: String? = null,
        val description2: String? = null,
        val stuType: String? = null,
        val isVisible: Boolean = true,
        val isStudentVisible: Boolean = true,
        val isStudentInput: Boolean = true,
        val isMulti: Boolean = true
    )

    /**
     * Item과 Semester 조회
     * */
    class InfoV3(
        val id: Int?,
        val name: String = "",
        val isPortfolio: Boolean = false,
        val description1: String? = null,
        val description2: String? = null,
        val stuType: String? = null,
        val isVisible: Boolean = true,
        val isStudentVisible: Boolean = true,
        val isStudentInput: Boolean = true,
        val isMulti: Boolean = true,
        val semesterItems: List<SemesterItemDto.InfoV3>
    )

    class deleteFailureInfo(
        val id: Int?,
        val name: String = ""
    )
}
