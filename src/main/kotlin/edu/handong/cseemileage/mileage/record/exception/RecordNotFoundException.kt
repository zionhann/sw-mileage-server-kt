package edu.handong.cseemileage.mileage.record.exception

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class RecordNotFoundException(
    info: String = ExceptionMessage.RECORD_NOT_FOUND
) : MileageException(info)
