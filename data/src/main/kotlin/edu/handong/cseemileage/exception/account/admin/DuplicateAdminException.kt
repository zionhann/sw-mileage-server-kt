package edu.handong.cseemileage.exception.account.admin

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class DuplicateAdminException(
    info: String = ExceptionMessage.DUPLICATE_ADMIN_EXCEPTION
) : MileageException(info)
