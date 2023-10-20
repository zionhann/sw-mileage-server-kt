package edu.handong.cseemileage.excel.strategy

import edu.handong.cseemileage.dto.excel.ExcelDto
import edu.handong.cseemileage.excel.strategy.header.MileageRecordUpload
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined

class MileageRecordFormat : DownloadStrategy {
    override var semester: String = ""
    override var description: String = "학생 마일리지 기록 엑셀 업로드 양식"

    override fun getHSSFColor(): Short {
        return HSSFColorPredefined.AQUA.index
    }

    override fun getExcelDtoList(): List<ExcelDto> {
        return MileageRecordUpload.entries
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
