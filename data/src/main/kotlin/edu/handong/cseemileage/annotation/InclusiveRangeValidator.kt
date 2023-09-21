package edu.handong.cseemileage.annotation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class InclusiveRangeValidator : ConstraintValidator<InclusiveRange, Int?> {
    private var minValue: Int = 0
    private var maxValue: Int = 0

    override fun initialize(annotation: InclusiveRange) {
        minValue = annotation.min
        maxValue = annotation.max
    }

    override fun isValid(value: Int?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return true // null 값은 검증을 통과합니다.
        }

        return value in minValue..maxValue
    }
}
