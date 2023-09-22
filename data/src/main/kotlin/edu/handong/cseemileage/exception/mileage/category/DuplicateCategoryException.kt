package edu.handong.cseemileage.exception.mileage.category

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class DuplicateCategoryException(
    info: String = ExceptionMessage.CATEGORY_DUPLICATE
) : MileageException(info)
