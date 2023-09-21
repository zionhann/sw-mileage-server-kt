package edu.handong.cseemileage.exception.mileage.category

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class CategoryCannotDeleteException(
    info: String = ExceptionMessage.CATEGORY_CANNOT_DELETE
) : MileageException(info)
