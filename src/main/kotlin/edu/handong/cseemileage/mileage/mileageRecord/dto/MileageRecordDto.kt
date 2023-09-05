package edu.handong.cseemileage.mileage.mileageRecord.dto

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemDto
import edu.handong.cseemileage.student.dto.StudentDto

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
        val student: StudentDto.Info? = null,
        val counts: Float? = null,
        val points: Int? = null,
        val extraPoints: Int? = null,
        val description1: String? = null,
        val description2: String? = null,
        val modDate: String? = null
    )

    class DeleteFailureInfo(
        val id: Int,
        val semesterItemName: String? = "",
        val studentName: String? = "",
        val points: Int? = null,
        val semester: String? = null
    )
}
