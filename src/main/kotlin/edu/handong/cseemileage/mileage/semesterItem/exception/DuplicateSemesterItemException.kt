package edu.handong.cseemileage.mileage.semesterItem.exception

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class DuplicateSemesterItemException(
    info: String = ExceptionMessage.SEMESTER_ITEM_DUPLICATE
) : MileageException(info)
