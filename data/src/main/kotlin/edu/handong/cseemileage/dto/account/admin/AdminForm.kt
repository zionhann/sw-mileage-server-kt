package edu.handong.cseemileage.dto.account.admin

import edu.handong.cseemileage.annotation.InclusiveRange
import edu.handong.cseemileage.exception.ExceptionMessage
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

class AdminForm(
    // nullable = false, default(X)
    @Schema(description = "관리자 이름", example = "장소연")
    @field:NotBlank(message = ExceptionMessage.ADMIN_NAME_IS_EMPTY)
    val name: String?,

    @Schema(description = "관리자 이메일", example = "admin@handong.ac.kr")
    @field:NotBlank(message = ExceptionMessage.ADMIN_EMAIL_IS_EMPTY)
    @field:Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = ExceptionMessage.INVALID_EMAIL)
    val email: String?,

    // nullable = true
    @Schema(description = "관리자 권한(0~3)", example = "2")
    @field:InclusiveRange(min = 0, max = 3, field = "권한(level)")
    val level: Int?
)
