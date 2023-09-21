package edu.handong.cseemileage.exception.account.student

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class StudentCannotDeleteException(
    info: String = ExceptionMessage.STUDENT_CANNOT_DELETE
) : MileageException(info)
