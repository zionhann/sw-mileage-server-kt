package edu.handong.cseemileage.exception.mileage.item

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class DuplicateItemException(
    info: String = ExceptionMessage.ITEM_DUPLICATE
) : MileageException(info)
