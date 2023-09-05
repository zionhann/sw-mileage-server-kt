package edu.handong.cseemileage.mileage.mileageRecord.dto

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemDto
import edu.handong.cseemileage.student.dto.StudentDto

@JsonInclude(JsonInclude.Include.NON_NULL)
class MileageRecordDto(
    val records: List<Info>? = null,
    val deleteFailureReasons: List<DeleteFailureInfo>? = null
) {

    class Info(
        val id: Int = 0,
        val semesterItem: SemesterItemDto.InfoV5 = SemesterItemDto.InfoV5(), // semesterItem, item
        val category: CategoryDto.InfoV1 = CategoryDto.InfoV1(),
        val student: StudentDto.Info = StudentDto.Info(),
        val counts: Float? = 1f,
        val points: Int? = null,
        val extraPoints: Int? = null,
        val description1: String? = null,
        val description2: String? = null
    )

    class InfoV2(
        val id: Int = 0,
        val studentName: String? = "",
        val studentSid: String? = "",
        val counts: Float? = 1f,
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
