package edu.handong.cseemileage.mileage.semesterItem.service

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.category.repository.CategoryRepository
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordDto
import edu.handong.cseemileage.mileage.mileageRecord.repository.MileageRecordRepository
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemDto
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class SemesterItemQueryService(
    val repository: SemesterItemRepository,
    val categoryRepository: CategoryRepository,
    val recordRepository: MileageRecordRepository,
    val modelMapper: ModelMapper
) {
    fun getSemesterItemsV1(semesterItems: String): List<SemesterItemDto.InfoV1> {
        val semesterItems = repository.findAllBySemesterName(semesterItems)
        return semesterItems.map {
            SemesterItemDto.InfoV1(
                id = it.id,
                item = modelMapper.map(it.item, ItemDto.InfoV2::class.java),
                category = modelMapper.map(it.category, CategoryDto.InfoV1::class.java),
                semesterName = it.semesterName,
                points = it.pointValue,
                itemMaxPoints = it.itemMaxPoints,
                categoryMaxPoints = it.categoryMaxPoints
            )
        }
    }

    fun getSemesterItemsWithRecords(semesterName: String): List<SemesterItemDto.InfoV4> {
        // 해당 학기 record 전체 조회
        val recordList = recordRepository.findAllBySemesterName(semesterName)

        // record가 기록된 학기 정보 중복 제거
        val semesterItems = recordList.map { it.semesterItem }
        val distinctSemesterItems = semesterItems.distinct()

        // 반환 데이터 조립
        return distinctSemesterItems.map { semesterItem ->
            val recordInfos = semesterItem.records.map { record ->
                MileageRecordDto.InfoV2(
                    id = record.id,
                    studentName = record.student.name,
                    counts = record.counts,
                    points = record.points,
                    extraPoints = record.extraPoints,
                    description1 = record.description1,
                    description2 = record.description2
                )
            }

            SemesterItemDto.InfoV4(
                id = semesterItem.id,
                itemName = semesterItem.item.name,
                categoryName = semesterItem.category.name,
                semesterName = semesterItem.semesterName,
                points = semesterItem.pointValue,
                itemMaxPoints = semesterItem.itemMaxPoints,
                categoryMaxPoints = semesterItem.categoryMaxPoints,
                records = recordInfos
            )
        }
    }
}
