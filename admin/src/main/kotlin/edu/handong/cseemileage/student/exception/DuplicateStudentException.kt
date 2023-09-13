package edu.handong.cseemileage.student.exception

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class DuplicateStudentException(
    info: String = ExceptionMessage.STUDENT_DUPLICATE
) : MileageException(info)
