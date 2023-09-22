package edu.handong.cseemileage.exception.mileage.semesterItem

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class SemesterItemCannotDeleteException(
    info: String = ExceptionMessage.SEMESTER_ITEM_CANNOT_DELETE
) : MileageException(info)
