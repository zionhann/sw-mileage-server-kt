package edu.handong.cseemileage.utils

class Utils {

    companion object {
        fun booleanToString(value: Boolean): String {
            return if (value) "Y" else "N"
        }

        fun stringToBoolean(value: String): Boolean {
            return value == "Y"
        }

        fun isTypeOfNumber(value: String): Boolean {
            return value.matches(Regex("^[+-]?\\d+(\\.\\d+)?$"))
        }
    }
}
