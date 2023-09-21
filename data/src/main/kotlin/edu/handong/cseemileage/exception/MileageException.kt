package edu.handong.cseemileage.exception

open class MileageException(val info: String?) : RuntimeException() {
    override val message: String?
        get() = super.message
}
