package edu.handong.cseemileage.exception.mileage.semesterItem

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class SemesterPatternException(
    info: String = ExceptionMessage.INVALID_SEMESTER_NAME
) : MileageException(info)
