package edu.handong.cseemileage.dto.mileage.item

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.dto.mileage.category.CategoryDto
import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemDto
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
class ItemDto(
    val description: String,
    val count: Int? = null,
    val list: List<Info>? = null,
    val data: Info? = null
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class Info(
        val id: Int? = null,
        val category: CategoryDto.Info? = null,
        val name: String? = null,
        val itemMaxPoints: Float? = null,
        val isPortfolio: Boolean? = null,
        val description1: String? = null,
        val description2: String? = null,
        val stuType: String? = null,
        val isVisible: Boolean? = null,
        val isStudentVisible: Boolean? = null,
        val isStudentInput: Boolean? = null,
        val isMulti: Boolean? = null,
        val modDate: LocalDateTime? = null,
        val semesterItems: List<SemesterItemDto.Info>? = null
    )
}
