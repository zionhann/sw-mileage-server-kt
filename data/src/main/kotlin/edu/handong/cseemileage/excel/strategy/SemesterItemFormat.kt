package edu.handong.cseemileage.excel.strategy

import edu.handong.cseemileage.dto.excel.ExcelDto
import edu.handong.cseemileage.excel.strategy.header.SemesterItemUpload
import org.apache.poi.hssf.util.HSSFColor

class SemesterItemFormat : DownloadStrategy {
    override var semester = ""
    override var description = "학기별 항목 업로드 양식"

    override fun getHSSFColor(): Short {
        return HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.index
    }

    override fun getExcelDtoList(): List<ExcelDto> {
        return SemesterItemUpload.entries
            .map { header ->
                ExcelDto(columnName = header.ko)
            }
    }

    override fun getCount(): Long {
        return 0
    }

    override fun getList(): List<*>? {
        return null
    }

    override fun getValue(obj: Any, fieldName: String, excelDtoType: String): Any {
        return Any()
    }
}
