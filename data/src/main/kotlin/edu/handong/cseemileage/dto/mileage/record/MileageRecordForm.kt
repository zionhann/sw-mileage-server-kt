package edu.handong.cseemileage.dto.mileage.record

import edu.handong.cseemileage.exception.ExceptionMessage
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

class MileageRecordForm(

    // nullable = false, default (x)
    @Schema(description = "학기별 항목 PK", example = "1")
    @field:Positive(message = ExceptionMessage.RECORD_SEMESTER_IS_NOT_POSITIVE)
    val semesterItemId: Int?,
    @Schema(description = "학생 이름", example = "홍길동")
    @field:NotBlank(message = ExceptionMessage.STUDENT_NAME_IS_EMPTY)
    val studentName: String?,
    @Schema(description = "학생 학번", example = "22000630")
    @field:NotBlank(message = ExceptionMessage.STUDENT_SID_IS_EMPTY)
    @field:Pattern(regexp = "^\\d{8}$", message = ExceptionMessage.STUDENT_INVALID_SID)
    val sid: String?,

    // nullable = true
    @Schema(description = "횟수(등록 갯수)", example = "1")
    @field:Positive(message = ExceptionMessage.RECORD_INVALID_COUNTS)
    val counts: Float?,
    @Schema(description = "가산점", example = "0")
    @field:PositiveOrZero(message = ExceptionMessage.RECORD_INVALID_EXTRA_POINTS)
    val extraPoints: Int?,
    @Schema(description = "마일리지 기록 설명1", example = "마일리지 기록 설명1")
    val description1: String?,
    @Schema(description = "마일리지 기록 설명1", example = "마일리지 기록 설명2")
    val description2: String?
)
