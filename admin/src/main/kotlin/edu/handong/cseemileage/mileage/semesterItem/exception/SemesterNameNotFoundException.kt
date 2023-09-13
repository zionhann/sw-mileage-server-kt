package edu.handong.cseemileage.mileage.semesterItem.exception

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class SemesterNameNotFoundException(
    info: String = ExceptionMessage.SEMESTER_NAME_NOT_FOUND
) : MileageException(info)
