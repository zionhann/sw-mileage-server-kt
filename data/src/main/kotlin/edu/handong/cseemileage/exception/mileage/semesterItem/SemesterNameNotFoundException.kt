package edu.handong.cseemileage.exception.mileage.semesterItem

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException
class SemesterNameNotFoundException(
    info: String = ExceptionMessage.SEMESTER_NAME_NOT_FOUND
) : MileageException(info)
