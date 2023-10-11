package edu.handong.cseemileage.exception.mileage.record

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class InvalidMileageRecordException(
    info: String = ExceptionMessage.INVALID_MILEAGE_RECORD
) : MileageException(info)
