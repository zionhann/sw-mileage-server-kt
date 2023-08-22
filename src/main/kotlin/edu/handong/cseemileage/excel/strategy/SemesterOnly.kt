package edu.handong.cseemileage.excel.strategy

import edu.handong.cseemileage.excel.ExcelUtils.Companion.addSemesterColumns
import edu.handong.cseemileage.excel.dto.ExcelDto
import edu.handong.cseemileage.mileage.semester.repository.SemesterRepository

class SemesterOnly(
    val semesterRepository: SemesterRepository
) : DownloadStrategy {
    override var semester: String = ""
    override var description: String = "전체 학기 항목 조회"
    override fun getExcelDtoList(): List<ExcelDto> {
        val list: MutableList<ExcelDto> = ArrayList()
        addSemesterColumns(list)
        return list
    }

    override fun getCount(): Long {
        return semesterRepository.count()
    }

    override fun getList(): List<*>? {
        return semesterRepository.findAll()
    }
}
