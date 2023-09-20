package edu.handong.cseemileage.admin.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
class AdminDto(
    val description: String,
    val count: Int? = null,
    val list: List<AdminDto.Info>? = null,
    val data: AdminDto.Info? = null
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class Info(
        val id: Int? = null,
        val name: String? = null,
        val email: String? = null,
        val level: Int? = null,
        val loginCount: Int? = null,
        val lastLoginDate: LocalDateTime? = null,
        val modDate: LocalDateTime? = null
    )
}
