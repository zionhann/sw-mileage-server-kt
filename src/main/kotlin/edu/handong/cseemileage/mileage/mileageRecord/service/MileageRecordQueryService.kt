package edu.handong.cseemileage.mileage.mileageRecord.service

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.mileageRecord.domain.MileageRecord
import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordDto
import edu.handong.cseemileage.mileage.mileageRecord.exception.MileageRecordNotFoundException
import edu.handong.cseemileage.mileage.mileageRecord.repository.MileageRecordRepository
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemDto
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
import edu.handong.cseemileage.student.dto.StudentDto
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class MileageRecordQueryService(
    val mileageRecordRepository: MileageRecordRepository,
    val semesterItemRepository: SemesterItemRepository,
    val modelMapper: ModelMapper
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

    private fun createMileageRecordDtoInfo(it: MileageRecord): MileageRecordDto.Info {
        return MileageRecordDto.Info(
            id = it.id,
            semesterItem = SemesterItemDto.Info(
                id = it.semesterItem.id,
                item = ItemDto.Info(
                    id = it.semesterItem.item.id,
                    name = it.semesterItem.item.name
                ),
                semesterName = it.semesterItem.semesterName,
                points = it.semesterItem.pointValue,
                itemMaxPoints = it.semesterItem.itemMaxPoints,
                categoryMaxPoints = it.semesterItem.categoryMaxPoints
            ),
            category = CategoryDto.Info(
                id = it.semesterItem.item.category.id,
                name = it.semesterItem.item.category.name
            ),
            student = StudentDto.Info(
                id = it.student.id,
                name = it.student.name,
                sid = it.student.sid
            ),
            counts = it.counts,
            points = it.points,
            extraPoints = it.extraPoints,
            description1 = it.description1,
            description2 = it.description2
        )
    }

    fun getRecordsByStudentId(studentId: Int): List<MileageRecordDto.Info> {
        val records = mileageRecordRepository.findAllByStudentId(studentId)
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
                    name = it.semesterItem.item.name
                ),
                semesterItem = SemesterItemDto.Info(
                    id = it.semesterItem.id,
                    semesterName = it.semesterItem.semesterName
                ),
                student = StudentDto.Info(
                    id = it.student.id,
                    name = it.student.name
                ),
                points = it.points
            )
        }
    }
}
