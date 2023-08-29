package edu.handong.cseemileage.student.exception

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class StudentNotFoundException(
    info: String = ExceptionMessage.STUDENT_NOT_FOUND
) : MileageException(info)
