package edu.handong.cseemileage.mileage.semesterItem.dto

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordDto

@JsonInclude(JsonInclude.Include.NON_NULL)
class SemesterItemDto(
    val description: String,
    val count: Int? = null,
    val list: List<Info>? = null,
    val data: Info? = null
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class Info(
        val id: Int? = null,
        val item: ItemDto.Info? = null,
        val category: CategoryDto.Info? = null,
        val semesterName: String? = null,
        val points: Float? = null,
        val itemMaxPoints: Float? = null,
        val records: List<MileageRecordDto.Info>? = null
    )
}
