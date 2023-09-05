package edu.handong.cseemileage.mileage.semesterItem.service

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordDto
import edu.handong.cseemileage.mileage.mileageRecord.repository.MileageRecordRepository
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemDto
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
import edu.handong.cseemileage.student.dto.StudentDto
import org.springframework.stereotype.Service

@Service
class SemesterItemQueryService(
    val repository: SemesterItemRepository,
    val categoryRepository: CategoryRepository,
    val recordRepository: MileageRecordRepository
) {
    fun getSemesterItemsV1(semesterItems: String): List<SemesterItemDto.Info> {
        val semesterItems = repository.findAllBySemesterName(semesterItems)
        return semesterItems.map {
            SemesterItemDto.Info(
                id = it.id,
                item = ItemDto.Info(
                    id = it.item.id,
                    name = it.item.name,
                    description1 = it.item.description1
                ),
                category = CategoryDto.Info(
                    id = it.item.category.id,
                    name = it.item.category.name
                ),
                semesterName = it.semesterName,
                points = it.pointValue,
                itemMaxPoints = it.itemMaxPoints,
                categoryMaxPoints = it.categoryMaxPoints
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
                    student = StudentDto.Info(
                        id = record.student.id,
                        name = record.student.name,
                        sid = record.student.sid
                    ),
                    counts = record.counts,
                    points = record.points,
                    extraPoints = record.extraPoints,
                    description1 = record.description1,
                    description2 = record.description2,
                    modDate = record.modDate.toString()
                )
            }

            SemesterItemDto.Info(
                id = semesterItem.id,
                item = ItemDto.Info(
                    id = semesterItem.item.id,
                    name = semesterItem.item.name
                ),
                category = CategoryDto.Info(
                    id = semesterItem.category.id,
                    name = semesterItem.category.name
                ),
                semesterName = semesterItem.semesterName,
                points = semesterItem.pointValue,
                itemMaxPoints = semesterItem.itemMaxPoints,
                categoryMaxPoints = semesterItem.categoryMaxPoints,
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
                categoryMaxPoints = it.categoryMaxPoints
            )
        }
    }
}
