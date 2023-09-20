package edu.handong.cseemileage.mileage.mileageRecord.service

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.mileageRecord.domain.MileageRecord
import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordDto
import edu.handong.cseemileage.mileage.mileageRecord.exception.MileageRecordNotFoundException
import edu.handong.cseemileage.mileage.mileageRecord.repository.MileageRecordRepository
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemDto
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
import org.springframework.stereotype.Service

@Service
class MileageRecordQueryService(
    val mileageRecordRepository: MileageRecordRepository,
    val semesterItemRepository: SemesterItemRepository
) {
    fun getRecordById(id: Int): MileageRecordDto.Info {
        mileageRecordRepository
            .findById(id)
            .orElseThrow(::MileageRecordNotFoundException)
            .let {
                return createMileageRecordDtoInfo(it)
            }
    }

    fun getRecords(): List<MileageRecordDto.Info> {
        val mileageRecords = mileageRecordRepository.findAllWithAllReferences()
        return mileageRecords.map {
            createMileageRecordDtoInfo(it)
        }
    }

    fun getRecordsBy(
        semesterItemId: Int
    ): List<MileageRecordDto.Info> {
        val mileageRecords = mileageRecordRepository.findAllBySemesterItemIdWithStudent(semesterItemId)
        return mileageRecords.map {
            MileageRecordDto.Info(
                id = it.id,
                studentName = it.name,
                sid = it.sid,
                counts = it.counts,
                points = it.points,
                extraPoints = it.extraPoints,
                description1 = it.description1,
                description2 = it.description2,
                modDate = it.modDate
            )
        }
    }

    private fun createMileageRecordDtoInfo(it: MileageRecord): MileageRecordDto.Info {
        return MileageRecordDto.Info(
            id = it.id,
            semesterItem = SemesterItemDto.Info(
                id = it.semesterItem.id,
                item = ItemDto.Info(
                    id = it.semesterItem.item.id,
                    name = it.semesterItem.item.name,
                    modDate = it.semesterItem.item.modDate
                ),
                semesterName = it.semesterItem.semesterName,
                points = it.semesterItem.pointValue,
                itemMaxPoints = it.semesterItem.itemMaxPoints,
                modDate = it.semesterItem.modDate
            ),
            category = CategoryDto.Info(
                id = it.semesterItem.category.id,
                name = it.semesterItem.category.name,
                categoryMaxPoints = it.semesterItem.category.categoryMaxPoints,
                modDate = it.semesterItem.category.modDate
            ),
            studentName = it.name,
            sid = it.sid,
            counts = it.counts,
            points = it.points,
            extraPoints = it.extraPoints,
            description1 = it.description1,
            description2 = it.description2,
            modDate = it.modDate
        )
    }

    fun getRecordsByStudentId(sid: String): List<MileageRecordDto.Info> {
        val records = mileageRecordRepository.findAllByStudentId(sid)
        return recordsToDeleteFailureInfo(records)
    }

    fun getRecordsBySemesterItemId(semesterItemId: Int): List<MileageRecordDto.Info> {
        val records = mileageRecordRepository.findAllBySemesterItemId(semesterItemId)
        return recordsToDeleteFailureInfo(records)
    }

    private fun recordsToDeleteFailureInfo(records: List<MileageRecord>): List<MileageRecordDto.Info> {
        return records.map {
            MileageRecordDto.Info(
                id = it.id,
                item = ItemDto.Info(
                    id = it.semesterItem.item.id,
                    name = it.semesterItem.item.name,
                    modDate = it.semesterItem.item.modDate
                ),
                semesterItem = SemesterItemDto.Info(
                    id = it.semesterItem.id,
                    semesterName = it.semesterItem.semesterName,
                    modDate = it.semesterItem.modDate
                ),
                studentName = it.name,
                sid = it.sid,
                points = it.points,
                modDate = it.modDate
            )
        }
    }
}
