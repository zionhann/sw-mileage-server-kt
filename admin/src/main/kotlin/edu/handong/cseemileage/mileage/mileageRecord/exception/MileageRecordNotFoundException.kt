package edu.handong.cseemileage.mileage.mileageRecord.exception

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class MileageRecordNotFoundException(
    info: String = ExceptionMessage.RECORD_NOT_FOUND
) : MileageException(info)
