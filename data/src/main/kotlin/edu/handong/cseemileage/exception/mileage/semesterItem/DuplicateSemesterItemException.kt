package edu.handong.cseemileage.exception.mileage.semesterItem

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class DuplicateSemesterItemException(
    info: String = ExceptionMessage.SEMESTER_ITEM_DUPLICATE
) : MileageException(info)
