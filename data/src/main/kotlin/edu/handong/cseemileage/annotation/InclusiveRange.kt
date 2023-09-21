package edu.handong.cseemileage.annotation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Constraint(validatedBy = [InclusiveRangeValidator::class])
annotation class InclusiveRange(
    val min: Int,
    val max: Int,
    val field: String,
    val message: String = "{field} 값의 범위가 유효하지 않습니다. [{min}, {max}]",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
