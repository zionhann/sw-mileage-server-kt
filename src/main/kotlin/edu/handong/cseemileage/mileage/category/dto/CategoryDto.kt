package edu.handong.cseemileage.mileage.category.dto

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.mileage.item.dto.ItemDto

@JsonInclude(JsonInclude.Include.NON_NULL)
class CategoryDto(
    val description: String,
    val count: Int? = null,
    val list: List<Info>? = null,
    val data: Info? = null
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class Info(
        val id: Int? = null,
        val name: String? = null,
        val description1: String? = null,
        val description2: String? = null,
        val orderIdx: Int? = null,
        val itemType: String? = null,
        val isMulti: Boolean? = null,
        val items: List<ItemDto.Info>? = null
    )
}
