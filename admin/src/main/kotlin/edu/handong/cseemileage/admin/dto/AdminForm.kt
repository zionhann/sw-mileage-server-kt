package edu.handong.cseemileage.admin.dto

import edu.handong.cseemileage.exception.ExceptionMessage
import javax.validation.constraints.NotBlank

class AdminForm(
    // nullable = false, default(X)
    @field:NotBlank(message = ExceptionMessage.ADMIN_NAME_IS_EMPTY)
    val name: String?,
    @field:NotBlank(message = ExceptionMessage.ADMIN_EMAIL_IS_EMPTY)
    val email: String?,

    // nullable = true
    val level: Int?
)
