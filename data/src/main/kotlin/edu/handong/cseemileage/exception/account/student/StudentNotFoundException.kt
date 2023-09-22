package edu.handong.cseemileage.exception.account.student

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class StudentNotFoundException(
    info: String = ExceptionMessage.STUDENT_NOT_FOUND
) : MileageException(info)
