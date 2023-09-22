package edu.handong.cseemileage.service.excel

import edu.handong.cseemileage.repository.mileage.CategoryRepository
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ExcelService(
    val categoryRepository: CategoryRepository
) {

    fun save(file: MultipartFile) {
        val workbook = WorkbookFactory.create(file.inputStream)
        val sheet = workbook.getSheetAt(0)
        val rows = sheet.physicalNumberOfRows

        for (i in 1 until rows) {
            val row = sheet.getRow(i)
            val cell = row.getCell(0)
            val value = cell.stringCellValue
            println(value)
        }
    }
}
