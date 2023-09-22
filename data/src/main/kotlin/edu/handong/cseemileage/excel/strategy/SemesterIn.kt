package edu.handong.cseemileage.excel.strategy

import edu.handong.cseemileage.dto.excel.ExcelDto
import edu.handong.cseemileage.excel.strategy.DownloadStrategy.Companion.EXCEL_DTO_CATEGORY
import edu.handong.cseemileage.excel.strategy.DownloadStrategy.Companion.EXCEL_DTO_ITEM
import edu.handong.cseemileage.excel.strategy.DownloadStrategy.Companion.addCategoryColumns
import edu.handong.cseemileage.excel.strategy.DownloadStrategy.Companion.addItemColumns
import edu.handong.cseemileage.excel.strategy.DownloadStrategy.Companion.addSemesterItemColumns
import edu.handong.cseemileage.repository.mileage.SemesterItemRepository

class SemesterIn(
    val semesterItemRepository: SemesterItemRepository
) : DownloadStrategy {
    override var semester: String = ""
    override var description: String = "학기별 항목 조회"
    override fun getHSSFColor(): Short {
        return super.getSemesterHSSFColor()
    }

    override fun getExcelDtoList(): List<ExcelDto> {
        val list: MutableList<ExcelDto> = ArrayList()
        addCategoryColumns(list)
        addItemColumns(list)
        addSemesterItemColumns(list)
        return list
    }

    override fun getCount(): Long {
        return semesterItemRepository.countBySemesterName(semester)
    }

    override fun getList(): List<*>? {
        return semesterItemRepository.findAllWithItemAndCategory(semester)
        return null
    }

    override fun getValue(obj: Any, fieldName: String, excelDtoType: String): Any {
        if (excelDtoType == EXCEL_DTO_ITEM) {
            return super.getItemValue(obj, fieldName)
        } else if (excelDtoType == EXCEL_DTO_CATEGORY) {
            val itemObj = getItemObj(obj)
            val categoryObj = getCategoryObj(itemObj)
            return super.getBasicValue(categoryObj, fieldName)
        }
        return getBasicValue(obj, fieldName)
    }
}
