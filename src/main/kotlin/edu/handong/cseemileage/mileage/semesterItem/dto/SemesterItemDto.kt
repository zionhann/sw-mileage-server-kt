package edu.handong.cseemileage.mileage.semesterItem.dto

import com.fasterxml.jackson.annotation.JsonInclude
import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordDto

@JsonInclude(JsonInclude.Include.NON_NULL)
class SemesterItemDto(
    val semesterItems: List<InfoV1>? = null,
    val semesterItemsWithRecords: List<InfoV4>? = null,
    val deleteFailureReasons: List<InfoV3>? = null
) {
    class InfoV1(
        val id: Int = 0,
        val item: ItemDto.InfoV2 = ItemDto.InfoV2(),
        val category: CategoryDto.InfoV1 = CategoryDto.InfoV1(),
        val semesterName: String = "2023-02",
        val points: Float = 0f,
        val itemMaxPoints: Float = 0f,
        val categoryMaxPoints: Float = 0f
    )

    @Deprecated("카테고리 > item 리스트 > 학기별 item 리스트 조회 개발 중 생성")
    class InfoV2(
        val category: CategoryDto.InfoV3
    )

    /**
     * semesterItem 단독 조회
     * */
    class InfoV3(
        val id: Int = 0,
        val semesterName: String = "2023-02",
        val points: Float = 0f,
        val itemMaxPoints: Float = 0f,
        val categoryMaxPoints: Float = 0f
    )

    class InfoV4(
        val id: Int = 0,
        val itemName: String = "",
        val categoryName: String = "",
        val semesterName: String = "2023-02",
        val points: Float = 0f,
        val itemMaxPoints: Float = 0f,
        val categoryMaxPoints: Float = 0f,
        val records: List<MileageRecordDto.InfoV2> = mutableListOf()
    )

    class InfoV5(
        val id: Int = 0,
        val item: ItemDto.InfoV2 = ItemDto.InfoV2(),
        val semesterName: String = "2023-02",
        val points: Float = 0f,
        val itemMaxPoints: Float = 0f,
        val categoryMaxPoints: Float = 0f
    )
}
