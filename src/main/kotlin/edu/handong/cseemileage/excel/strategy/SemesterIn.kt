package edu.handong.cseemileage.excel.strategy

import edu.handong.cseemileage.excel.ExcelUtils
import edu.handong.cseemileage.excel.ExcelUtils.Companion.addCategoryColumns
import edu.handong.cseemileage.excel.ExcelUtils.Companion.addItemColumns
import edu.handong.cseemileage.excel.ExcelUtils.Companion.addSemesterColumns
import edu.handong.cseemileage.excel.dto.ExcelDto
import edu.handong.cseemileage.mileage.semester.repository.SemesterRepository

class SemesterIn(
    val semesterRepository: SemesterRepository
) : DownloadStrategy {
    override var semester: String = ""
    override var description: String = "학기별 항목 조회"
    override fun getExcelDtoList(): List<ExcelDto> {
        val list: MutableList<ExcelDto> = ArrayList()
        addCategoryColumns(list)
        addItemColumns(list)
        addSemesterColumns(list)
        return list
    }

    override fun getCount(): Long {
        return semesterRepository.countByName(semester)
    }

    override fun getList(): List<*>? {
        return semesterRepository.findAllWithItemAndCategory(semester)
    }

    override fun getValue(obj: Any, fieldName: String, excelDtoType: String): Any {
        if (excelDtoType == ExcelUtils.EXCEL_DTO_ITEM) {
            return super.getItemValue(obj, fieldName)
        } else if (excelDtoType == ExcelUtils.EXCEL_DTO_CATEGORY) {
            val itemObj = getItemObj(obj)
            val categoryObj = getCategoryObj(itemObj)
            return super.getBasicValue(categoryObj, fieldName)
        }
        return getBasicValue(obj, fieldName)
    }
}
