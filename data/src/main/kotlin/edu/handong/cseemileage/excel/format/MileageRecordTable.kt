package edu.handong.cseemileage.excel.format

import edu.handong.cseemileage.domain.mileage.MileageRecord
import edu.handong.cseemileage.domain.mileage.SemesterItem
import edu.handong.cseemileage.excel.numericToString
import edu.handong.cseemileage.exception.mileage.semesterItem.SemesterItemNotFoundException
import edu.handong.cseemileage.repository.mileage.MileageRecordRepository
import edu.handong.cseemileage.repository.mileage.SemesterItemRepository
import org.apache.poi.ss.usermodel.Row

/**
 * 마일리지 기록 엑셀 양식
 *
 * 1. 카테고리
 * 2. 학기
 * 3. 항목명
 * 4. 학번
 * 5. 이름
 * 6. 등록횟수
 * 7. 가산점
 * 8. 추가설명1
 * 9. 추가설명2
 */
class MileageRecordTable(
    row: Row,
    private val semesterItemRepository: SemesterItemRepository,
    private val mileageRecordRepository: MileageRecordRepository
) {
    val category: String = row.getCell(1).stringCellValue
    val semester: String = row.getCell(2).stringCellValue
    val itemName: String = row.getCell(3).stringCellValue
    val sid: String = numericToString(row.getCell(4))
    val studentName: String = row.getCell(5).stringCellValue
    val count: Int = row.getCell(6).numericCellValue.toInt()
    val extraPoints: Int = row.getCell(6).numericCellValue.toInt()
    val description1: String = row.getCell(7).stringCellValue
    val description2: String = row.getCell(8).stringCellValue

    fun toSemesterItem(): SemesterItem {
        return semesterItemRepository.findBySemesterNameAndItemName(semester, itemName)
            ?: throw SemesterItemNotFoundException()
    }

    fun toMileageRecord(semesterItem: SemesterItem): MileageRecord {
        return mileageRecordRepository.findBySidAndSemesterItem(sid, semesterItem)
            ?.let { record ->
                // if mileage record already exists, update it
                record.extraPoints = extraPoints
                record.description1 = description1
                record.description2 = description2
                return record
            } ?: let {
            // if mileage record does not exist, create new one
            return MileageRecord(semesterItem, studentName, sid).also { record ->
                record.extraPoints = extraPoints
                record.description1 = description1
                record.description2 = description2

                semesterItem.addRecord(record)
            }
        }
    }
}
