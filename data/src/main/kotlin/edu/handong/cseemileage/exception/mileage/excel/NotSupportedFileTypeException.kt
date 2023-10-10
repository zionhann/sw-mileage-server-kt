package edu.handong.cseemileage.exception.mileage.excel

import edu.handong.cseemileage.exception.ExceptionMessage

class NotSupportedFileTypeException(
    info: String = ExceptionMessage.NOT_SUPPORTED_FILE_TYPE
) : Exception(info)
