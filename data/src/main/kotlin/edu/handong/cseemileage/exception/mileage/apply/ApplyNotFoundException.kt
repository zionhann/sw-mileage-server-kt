package edu.handong.cseemileage.exception.mileage.apply

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class ApplyNotFoundException(
    info: String = ExceptionMessage.APPLY_NOT_FOUND_EXCEPTION
) : MileageException(info)
