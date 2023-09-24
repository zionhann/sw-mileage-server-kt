package edu.handong.cseemileage.dto.mileage.semesterItem

import com.sun.istack.NotNull
import edu.handong.cseemileage.exception.ExceptionMessage
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

data class SemesterItemForm(
    // nullable = false, default (X)
    @Schema(description = "상위 항목 PK", example = "1")
    @field:Positive(message = ExceptionMessage.SEMESTER_ITEM_ID_IS_NOT_POSITIVE)
    @field:NotNull
    val itemId: Int?,

    // nullable = false, default (O)
    @Schema(description = "학기별 항목의 마일리지 점수", example = "3")
    @field:PositiveOrZero(message = ExceptionMessage.SEMESTER_ITEM_POINTS_IS_NEGATIVE)
    val points: Float?,
    @Schema(description = "해당 학기에 상위 항목 내에서 획득 가능한 최대 점수", example = "15")
    @field:PositiveOrZero(message = ExceptionMessage.ITEM_MAX_POINTS_IS_NEGATIVE)
    val itemMaxPoints: Float?,

    // update 폼에서만 사용. create: PathVariable 사용
    @Schema(description = "등록 학기 변경", example = "2023-02")
    @field:Pattern(regexp = "^(\\d{4}-(01|02))$", message = ExceptionMessage.INVALID_SEMESTER_NAME)
    val semesterName: String?
)
