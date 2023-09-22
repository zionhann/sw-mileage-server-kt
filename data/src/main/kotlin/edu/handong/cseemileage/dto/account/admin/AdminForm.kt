package edu.handong.cseemileage.dto.account.admin

import edu.handong.cseemileage.annotation.InclusiveRange
import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

class AdminForm(
    // nullable = false, default(X)
    @field:NotBlank(message = ExceptionMessage.ADMIN_NAME_IS_EMPTY)
    val name: String?,
    @field:NotBlank(message = ExceptionMessage.ADMIN_EMAIL_IS_EMPTY)
    @field:Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = ExceptionMessage.INVALID_EMAIL)
    val email: String?,

    // nullable = true
    @field:InclusiveRange(min = 0, max = 10, field = "권한(level)")
    val level: Int?
)
