package edu.handong.cseemileage.dto.account.student

import edu.handong.cseemileage.annotation.InclusiveRange
import edu.handong.cseemileage.exception.ExceptionMessage
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

class StudentForm(
    // nullable = true
    @Schema(description = "학생 이름", example = "장유진")
    val name: String?,

    @Schema(description = "학생 학번 - 필수 요청. 8자리 숫자", example = "22000630")
    @field:NotBlank(message = ExceptionMessage.STUDENT_SID_IS_EMPTY)
    @field:Pattern(regexp = "^\\d{8}$", message = ExceptionMessage.STUDENT_INVALID_SID)
    val sid: String?,

    @Schema(description = "학생 학부", example = "전산전자공학부")
    val department: String?,
    @Schema(description = "학생 1전공", example = "컴퓨터공학")
    val major1: String?,
    @Schema(description = "학생 2전공", example = "전자공학")
    val major2: String?,
    @Schema(description = "학생 학년", example = "3")
    @field:InclusiveRange(min = 1, max = 4, field = "학년")
    val year: Int?,
    @Schema(description = "학생 학기 수", example = "6")
    @field:InclusiveRange(min = 1, max = 8, field = "학기 수")
    val semesterCount: Int?,

    // nullable = false, default (O)
    @Schema(description = "-- 아직 나도 잘 모름 --", example = "true")
    val isChecked: Boolean?

    // loginCount,lastLoginDate는 create, update 시 요청을 받지 않는 필드이므로 제외
)
