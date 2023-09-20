package edu.handong.cseemileage.apply.exception

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class ApplyNotFoundException(
    info: String = ExceptionMessage.APPLY_NOT_FOUND_EXCEPTION
) : MileageException(info)
