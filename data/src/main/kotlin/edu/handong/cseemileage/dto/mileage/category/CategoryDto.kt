package edu.handong.cseemileage.dto.mileage.category

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.dto.mileage.item.ItemDto
import java.time.LocalDateTime

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
        val categoryMaxPoints: Float? = null,
        val description1: String? = null,
        val description2: String? = null,
        val orderIdx: Int? = null,
        val type: String? = null,
        val modDate: LocalDateTime? = null,
        val items: List<ItemDto.Info>? = null,
        val itemCount: Int? = null
    )
}
