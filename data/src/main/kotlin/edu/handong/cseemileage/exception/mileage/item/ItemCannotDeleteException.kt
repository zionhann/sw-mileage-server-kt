package edu.handong.cseemileage.exception.mileage.item

import edu.handong.cseemileage.exception.ExceptionMessage
import edu.handong.cseemileage.exception.MileageException

class ItemCannotDeleteException(
    info: String = ExceptionMessage.ITEM_CANNOT_DELETE
) : MileageException(info)
