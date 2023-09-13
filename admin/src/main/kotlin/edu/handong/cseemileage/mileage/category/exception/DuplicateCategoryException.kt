package edu.handong.cseemileage.mileage.category.exception

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class DuplicateCategoryException(
    info: String = ExceptionMessage.CATEGORY_DUPLICATE
) : MileageException(info)
