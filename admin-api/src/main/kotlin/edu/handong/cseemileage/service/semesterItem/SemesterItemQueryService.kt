package edu.handong.cseemileage.service.semesterItem

import edu.handong.cseemileage.domain.mileage.Category
import edu.handong.cseemileage.domain.mileage.Item
import edu.handong.cseemileage.domain.mileage.SemesterItem
import edu.handong.cseemileage.dto.mileage.category.CategoryDto
import edu.handong.cseemileage.dto.mileage.item.ItemDto
import edu.handong.cseemileage.dto.mileage.record.MileageRecordDto
import edu.handong.cseemileage.dto.mileage.semesterItem.SemesterItemDto
import edu.handong.cseemileage.repository.mileage.CategoryRepository
import edu.handong.cseemileage.repository.mileage.MileageRecordRepository
import edu.handong.cseemileage.repository.mileage.SemesterItemRepository
import edu.handong.cseemileage.utils.Utils.Companion.stringToBoolean
import org.springframework.stereotype.Service

@Service
class SemesterItemQueryService(
    val repository: SemesterItemRepository,
    val categoryRepository: CategoryRepository,
    val recordRepository: MileageRecordRepository
) {
    fun getSemesterItemsV1(semesterItems: String): List<SemesterItemDto.Info> {
        val results = repository.findAllWithItemAndCategoryAndRecordCount(semesterItems)
        var item: Item
        var category: Category
        var recordCount: Long
        var semesterItem: SemesterItem
        return results.map { result ->
            item = result.item
            category = result.category
            recordCount = result.recordCount
            semesterItem = result.semesterItem
            SemesterItemDto.Info(
                id = semesterItem.id,
                item = ItemDto.Info(
                    id = item.id,
                    name = item.name,
                    description1 = item.description1,
                    modDate = item.modDate,
                    semesterItemCount = item.semesterItems.size
                ),
                category = CategoryDto.Info(
                    id = category.id,
                    name = category.name,
                    categoryMaxPoints = category.categoryMaxPoints,
                    modDate = category.modDate
                ),
                semesterName = semesterItem.semesterName,
                points = semesterItem.pointValue,
                itemMaxPoints = semesterItem.itemMaxPoints,
                isMulti = stringToBoolean(semesterItem.isMulti),
                modDate = semesterItem.modDate,
                recordCount = recordCount.toInt()
            )
        }
    }

    fun getSemesterItemsWithRecords(semesterName: String): List<SemesterItemDto.Info> {
        // 해당 학기 record 전체 조회
        val recordList = recordRepository.findAllBySemesterName(semesterName)

        // record가 기록된 학기 정보 중복 제거
        val semesterItems = recordList.map { it.semesterItem }
        val distinctSemesterItems = semesterItems.distinct()

        // 반환 데이터 조립
        return distinctSemesterItems.map { semesterItem ->
            val recordInfos = semesterItem.records.map { record ->
                MileageRecordDto.Info(
                    id = record.id,
                    studentName = record.name,
                    sid = record.sid,
                    counts = record.counts,
                    points = record.points,
                    extraPoints = record.extraPoints,
                    description1 = record.description1,
                    description2 = record.description2,
                    modDate = record.modDate
                )
            }

            SemesterItemDto.Info(
                id = semesterItem.id,
                item = ItemDto.Info(
                    id = semesterItem.item.id,
                    name = semesterItem.item.name,
                    modDate = semesterItem.item.modDate,
                    semesterItemCount = semesterItems.size
                ),
                category = CategoryDto.Info(
                    id = semesterItem.category.id,
                    name = semesterItem.category.name,
                    categoryMaxPoints = semesterItem.category.categoryMaxPoints,
                    modDate = semesterItem.category.modDate
                ),
                semesterName = semesterItem.semesterName,
                points = semesterItem.pointValue,
                itemMaxPoints = semesterItem.itemMaxPoints,
                isMulti = stringToBoolean(semesterItem.isMulti),
                modDate = semesterItem.modDate,
                records = recordInfos
            )
        }
    }

    fun getSemesterItemByItemId(itemId: Int): List<SemesterItemDto.Info> {
        val semesterItems = repository.findAllByItemId(itemId)
        return semesterItems.map {
            SemesterItemDto.Info(
                id = it.id,
                semesterName = it.semesterName,
                points = it.pointValue,
                itemMaxPoints = it.itemMaxPoints,
                isMulti = stringToBoolean(it.isMulti),
                modDate = it.modDate
            )
        }
    }
}
