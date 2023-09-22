package edu.handong.cseemileage.dto.mileage.record

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.dto.mileage.category.CategoryDto
import edu.handong.cseemileage.dto.mileage.item.ItemDto
import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemDto
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
class MileageRecordDto(
    val description: String,
    val count: Int? = null,
    val list: List<Info>? = null,
    val data: Info? = null
) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    class Info(
        val id: Int? = null,
        val semesterItem: SemesterItemDto.Info? = null,
        val item: ItemDto.Info? = null,
        val category: CategoryDto.Info? = null,
        val studentName: String? = null,
        val sid: String? = null,
        val counts: Float? = null,
        val points: Int? = null,
        val extraPoints: Int? = null,
        val description1: String? = null,
        val description2: String? = null,
        val modDate: LocalDateTime? = null
    )
}
