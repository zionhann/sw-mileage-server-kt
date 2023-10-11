package edu.handong.cseemileage.exception.mileage.record

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class InvalidDuplicateMileageRecordException(
    info: String = ExceptionMessage.INVALID_DUPLICATE_MILEAGE_RECORD
) : MileageException(info)
