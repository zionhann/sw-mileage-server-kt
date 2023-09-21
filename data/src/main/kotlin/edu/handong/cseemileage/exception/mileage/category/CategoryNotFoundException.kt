package edu.handong.cseemileage.exception.mileage.category

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class CategoryNotFoundException(
    info: String = ExceptionMessage.CATEGORY_NOT_FOUND
) : MileageException(info)
