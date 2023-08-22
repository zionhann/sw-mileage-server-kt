package edu.handong.cseemileage.excel.strategy

import edu.handong.cseemileage.excel.dto.ExcelDto

interface DownloadStrategy {

    var semester: String
    var description: String
    fun getExcelDtoList(): List<ExcelDto>
    fun getCount(): Long
    fun getList(): List<*>?
}
