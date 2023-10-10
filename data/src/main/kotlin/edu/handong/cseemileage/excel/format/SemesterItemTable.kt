package edu.handong.cseemileage.excel.format

import edu.handong.cseemileage.domain.mileage.Item
import edu.handong.cseemileage.domain.mileage.SemesterItem
import edu.handong.cseemileage.exception.mileage.item.ItemNotFoundException
import edu.handong.cseemileage.repository.mileage.ItemRepository
import edu.handong.cseemileage.repository.mileage.SemesterItemRepository
import org.apache.poi.ss.usermodel.Row

/**
 * 1. 항목명
 * 2. 년도 및 학기 (yyyy-ss)
 * 3. 세부항목명
 * 4. 가중치
 * 5. 최대 마일리지
 * 6. 중복 레코드 허용 ('Y' | 'N')
 */
class SemesterItemTable(
    row: Row,
    private val itemRepository: ItemRepository,
    private val semesterItemRepository: SemesterItemRepository
) {

    val categoryName: String = row.getCell(1).stringCellValue
    val semester: String = row.getCell(2).stringCellValue
    val itemName: String = row.getCell(3).stringCellValue
    val pointValue: Float = row.getCell(4).numericCellValue.toFloat()
    val maxPoints: Float = row.getCell(5).numericCellValue.toFloat()
    val isDuplicatable: String = row.getCell(6).stringCellValue

    fun toItem(): Item {
        // item name should be unique over all categories
        return itemRepository.findByName(itemName)
            ?: throw ItemNotFoundException()
    }

    fun toSemesterItem(item: Item): SemesterItem {
        return semesterItemRepository.findBySemesterNameAndItemId(semester, item.id)
            ?.let { semesterItem ->
                // if semester item already exists, update it
                semesterItem.pointValue = pointValue
                semesterItem.itemMaxPoints = maxPoints
                semesterItem.semesterName = semester
                semesterItem.isMulti = isDuplicatable.ifBlank { "N" }
                return semesterItem
            } ?: let {
            // if semester item does not exist, create new one
            return SemesterItem(item, item.category).also { semesterItem ->
                semesterItem.pointValue = pointValue
                semesterItem.itemMaxPoints = maxPoints
                semesterItem.semesterName = semester
                semesterItem.isMulti = isDuplicatable.ifBlank { "N" }

                item.addSemesterItem(semesterItem)
            }
        }
    }
}
