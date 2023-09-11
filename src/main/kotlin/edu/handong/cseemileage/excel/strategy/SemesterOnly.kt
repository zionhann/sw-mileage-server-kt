package edu.handong.cseemileage.excel.strategy

import edu.handong.cseemileage.excel.dto.ExcelDto
import edu.handong.cseemileage.excel.strategy.DownloadStrategy.Companion.addSemesterItemColumns
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository

class SemesterOnly(
    val semesterItemRepository: SemesterItemRepository
) : DownloadStrategy {
    override var semester: String = ""
    override var description: String = "전체 학기 항목 조회"
    override fun getHSSFColor(): Short {
        return super.getSemesterHSSFColor()
    }

    override fun getExcelDtoList(): List<ExcelDto> {
        val list: MutableList<ExcelDto> = ArrayList()
        addSemesterItemColumns(list)
        return list
    }

    override fun getCount(): Long {
        return semesterItemRepository.count()
    }

    override fun getList(): List<*>? {
        return semesterItemRepository.findAll()
    }

    override fun getValue(obj: Any, fieldName: String, excelDtoType: String): Any {
        return super.getBasicValue(obj, fieldName)
    }
}
