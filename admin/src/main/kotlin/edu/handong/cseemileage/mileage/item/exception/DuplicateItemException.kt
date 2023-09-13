package edu.handong.cseemileage.mileage.item.exception

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class DuplicateItemException(
    info: String = ExceptionMessage.ITEM_DUPLICATE
) : MileageException(info)
