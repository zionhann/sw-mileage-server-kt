package edu.handong.cseemileage.dto.mileage.item

import edu.handong.cseemileage.exception.ExceptionMessage
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

data class ItemForm(
    // nullable = false, default (X)
    @Schema(description = "항목의 상위 카테고리 PK", example = "1")
    @field:Positive(message = ExceptionMessage.ITEM_CATEGORY_ID_NOT_POSITIVE)
    val categoryId: Int?,
    @Schema(description = "항목 이름", example = "캡스톤 디자인 특강1")
    @field:NotBlank(message = ExceptionMessage.ITEM_NAME_IS_EMPTY)
    val itemName: String?,

    // nullable = true
    @Schema(description = "항목 설명1", example = "3주차 시행")
    val description1: String?,
    @Schema(description = "항목 설명2", example = "명단 작성으로 인정")
    val description2: String?,
    @Schema(description = "항목 학생 유형", example = "CF")
    @field:Pattern(regexp = "^(|C|F|CF)$", message = ExceptionMessage.ITEM_INVALID_STU_TYPE)
    val stuType: String?,

    // nullable = false, default (O)
    @Schema(description = "항목별 최대 인정 마일리지", example = "10")
    @field:PositiveOrZero(message = ExceptionMessage.ITEM_MAX_POINTS_IS_NEGATIVE)
    val itemMaxPoints: Float?,
    @Schema(description = "항목 Boolean 값")
    val flags: Flag?
) {
    class Flag(
        @Schema(description = "보이기 여부", example = "true")
        val isVisible: Boolean?,
        @Schema(description = "학생에게 보이기 여부", example = "true")
        val isStudentVisible: Boolean?,
        @Schema(description = "학생 주도 등록 여부", example = "true")
        val isStudentEditable: Boolean?,
        @Schema(description = "포트폴리오 포함 여부", example = "true")
        val isPortfolio: Boolean?
    )
}
