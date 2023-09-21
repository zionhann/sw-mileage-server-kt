package edu.handong.cseemileage.exception.mileage.apply

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class DuplicateApplyException(
    info: String = ExceptionMessage.DUPLICATE_APPLY_EXCEPTION
) : MileageException(info)
