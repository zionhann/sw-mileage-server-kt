package edu.handong.cseemileage.dto.mileage.semesterItem

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.dto.mileage.category.CategoryDto
import edu.handong.cseemileage.dto.mileage.item.ItemDto
import edu.handong.cseemileage.dto.mileage.record.MileageRecordDto
import java.time.LocalDateTime

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
        val modDate: LocalDateTime? = null,
        val records: List<MileageRecordDto.Info>? = null
    )
}
