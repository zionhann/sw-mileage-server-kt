package edu.handong.cseemileage.dto.mileage.category

import edu.handong.cseemileage.exception.ExceptionMessage
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.PositiveOrZero

class CategoryForm(
    // nullable = false, default (X)
    @Schema(description = "카테고리 이름", example = "비교과 - 세미나 참여")
    @field:NotBlank(message = ExceptionMessage.CATEGORY_TITLE_IS_EMPTY)
    val title: String?,

    // nullable = false, default (O)
    @Schema(description = "카테고리 보여주는 순서", example = "4")
    val orderIdx: Int?,
    @Schema(description = "카테고리 하위에 올 수 있는 item 유형", example = "R")
    @field:Pattern(regexp = "^[R]$", message = ExceptionMessage.CATEGORY_INVALID_ITEM_TYPE)
    val itemType: String?,
    @Schema(description = "카테고리 하위에 여러 item을 가질 수 있는지 여부", example = "true")
    val isMulti: Boolean?,
    @Schema(description = "카테고리 최대 인정 마일리지", example = "20")
    @field:PositiveOrZero(message = ExceptionMessage.CATEGORY_MAX_POINTS_IS_NEGATIVE)
    val categoryMaxPoints: Float?,

    // nullable = true
    @Schema(description = "카테고리 설명1", example = "캡스톤 디자인 특강에 참여할 경우 마일리지 적립")
    val description1: String?,
    @Schema(description = "카테고리 설명2", example = "캡스톤 디자인을 수강하지 않는 전전 학생도 참여 가능")
    val description2: String?
)
