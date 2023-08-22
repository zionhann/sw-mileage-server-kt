package edu.handong.cseemileage.excel.strategy

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
}
