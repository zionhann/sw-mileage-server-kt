package edu.handong.cseemileage.mileage.category.exception

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class CategoryCannotDeleteException(
    info: String = ExceptionMessage.CATEGORY_CANNOT_DELETE
) : MileageException(info)
