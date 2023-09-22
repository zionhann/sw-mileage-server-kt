package edu.handong.cseemileage.exception.mileage.semesterItem

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class SemesterItemNotFoundException(
    info: String = ExceptionMessage.SEMESTER_ITEM_NOT_FOUND
) : MileageException(info)
