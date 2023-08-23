package edu.handong.cseemileage.mileage.item.exception

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class ItemNotFoundException(
    info: String = ExceptionMessage.ITEM_NOT_FOUND
) : MileageException(info)
