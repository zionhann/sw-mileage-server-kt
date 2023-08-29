package edu.handong.cseemileage.mileage.category.dto

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.mileage.item.dto.ItemDto

@JsonInclude(JsonInclude.Include.NON_NULL)
class CategoryDto(
    val categories: List<InfoV1>? = null,
    val categoriesWithItems: List<InfoV2>? = null,
    val categoriesWithItemsAndSemesters: List<InfoV3>? = null
) {

    /**
     * Category 단독 조회
     * */
    class InfoV1(
        val id: Int = 0,
        val name: String = "",
        val description1: String? = null,
        val description2: String? = null,
        val orderIdx: Int = 0,
        val itemType: String = "",
        val isMulti: Boolean = false
    )

    /**
     * Category와 Item 조회
     * */
    class InfoV2(
        val category: InfoV1,
        val items: List<ItemDto.InfoV2>
    )

    /**
     * Category와 Item, Semester 조회
     * */
    class InfoV3(
        val category: InfoV1,
        val items: List<ItemDto.InfoV3>
    )
}
