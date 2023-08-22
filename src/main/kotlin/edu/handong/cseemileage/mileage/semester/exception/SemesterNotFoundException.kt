package edu.handong.cseemileage.mileage.semester.exception

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class SemesterNotFoundException(
    info: String = ExceptionMessage.SEMESTER_NOT_FOUND
) : MileageException(info)
