package edu.handong.cseemileage.exception.account.admin

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class AdminNotFoundException(
    info: String = ExceptionMessage.ADMIN_NOT_FOUND_EXCEPTION
) : MileageException(info)
