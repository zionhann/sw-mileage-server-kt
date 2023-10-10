package edu.handong.cseemileage.service.excel

import edu.handong.cseemileage.domain.mileage.MileageRecord
import edu.handong.cseemileage.domain.mileage.SemesterItem
import edu.handong.cseemileage.excel.format.MileageRecordTable
import edu.handong.cseemileage.excel.format.SemesterItemTable
import edu.handong.cseemileage.excel.numericToString
import edu.handong.cseemileage.exception.mileage.excel.NotSupportedFileTypeException
import edu.handong.cseemileage.repository.mileage.ItemRepository
import edu.handong.cseemileage.repository.mileage.MileageRecordRepository
import edu.handong.cseemileage.repository.mileage.SemesterItemRepository
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ExcelService(
    private val itemRepository: ItemRepository,
    private val semesterItemRepository: SemesterItemRepository,
    private val mileageRecordRepository: MileageRecordRepository
) {

    fun <T> save(file: MultipartFile, classType: Class<T>): List<Int> {
        val workbook = WorkbookFactory.create(file.inputStream)
        val sheet = workbook.getSheetAt(0)
        val validRows = sheet
            .filter { row ->
                val indexCell = row.getCell(0)

                indexCell != null &&
                    numericToString(indexCell) != ""
            }
            .toList()

        val results = when (classType.simpleName) {
            "SemesterItem" -> {
                val semesterItemList = (1 until validRows.size).map { i ->
                    val row = sheet.getRow(i)
                    val excelData = SemesterItemTable(
                        row,
                        itemRepository,
                        semesterItemRepository
                    )
                    val item = excelData.toItem()
                    excelData.toSemesterItem(item)
                }.toList()
                semesterItemRepository.saveAll(semesterItemList).map(SemesterItem::id)
            }

            "MileageRecord" -> {
                val mileageRecordList = (1 until validRows.size).map { i ->
                    val row = sheet.getRow(i)
                    val excelData = MileageRecordTable(
                        row,
                        semesterItemRepository,
                        mileageRecordRepository
                    )
                    val semesterItem = excelData.toSemesterItem()
                    excelData.toMileageRecord(semesterItem)
                }.toList()
                mileageRecordRepository.saveAll(mileageRecordList).map(MileageRecord::id)
            }

            else -> throw NotSupportedFileTypeException()
        }
        return results
    }
}
