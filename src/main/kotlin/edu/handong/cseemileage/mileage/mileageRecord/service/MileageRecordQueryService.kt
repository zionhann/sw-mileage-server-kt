package edu.handong.cseemileage.mileage.mileageRecord.service

import edu.handong.cseemileage.mileage.category.dto.CategoryDto
import edu.handong.cseemileage.mileage.item.dto.ItemDto
import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordDto
import edu.handong.cseemileage.mileage.mileageRecord.exception.MileageRecordNotFoundException
import edu.handong.cseemileage.mileage.mileageRecord.repository.MileageRecordRepository
import edu.handong.cseemileage.mileage.semesterItem.dto.SemesterItemDto
import edu.handong.cseemileage.mileage.semesterItem.exception.SemesterItemNotFoundException
import edu.handong.cseemileage.mileage.semesterItem.repository.SemesterItemRepository
import edu.handong.cseemileage.student.dto.StudentDto
import edu.handong.cseemileage.student.exception.StudentNotFoundException
import edu.handong.cseemileage.student.repository.StudentRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class MileageRecordQueryService(
    val mileageRecordRepository: MileageRecordRepository,
    val semesterItemRepository: SemesterItemRepository,
    val studentRepository: StudentRepository,
    val modelMapper: ModelMapper
) {
    fun getRecordById(id: Int): MileageRecordDto.Info {
        mileageRecordRepository
            .findById(id)
            .orElseThrow(::MileageRecordNotFoundException)
            .let {
                return MileageRecordDto.Info(
                    id = it.id,
                    semesterItem = modelMapper.map(it.semesterItem, SemesterItemDto.InfoV5::class.java),
                    category = modelMapper.map(it.semesterItem.category, CategoryDto.InfoV1::class.java),
                    student = modelMapper.map(it.student, StudentDto.Info::class.java),
                    counts = it.counts,
                    points = it.points,
                    extraPoints = it.extraPoints,
                    description1 = it.description1,
                    description2 = it.description2
                )
            }
    }

    fun getRecords(): List<MileageRecordDto.Info> {
        val mileageRecords = mileageRecordRepository.findAll()
        return mileageRecords.map {
            val semesterItem = semesterItemRepository
                .findById(it.semesterItem.id!!)
                .orElseThrow(::SemesterItemNotFoundException)
            val student = studentRepository
                .findById(it.student.id!!)
                .orElseThrow(::StudentNotFoundException)
            MileageRecordDto.Info(
                id = it.id,
                semesterItem = SemesterItemDto.InfoV5(
                    id = it.semesterItem.id,
                    item = modelMapper.map(it.semesterItem.item, ItemDto.InfoV2::class.java),
                    semesterName = it.semesterItem.semesterName,
                    points = it.semesterItem.pointValue,
                    itemMaxPoints = it.semesterItem.itemMaxPoints,
                    categoryMaxPoints = it.semesterItem.categoryMaxPoints
                ),
                category = modelMapper.map(semesterItem.category, CategoryDto.InfoV1::class.java),
                student = modelMapper.map(student, StudentDto.Info::class.java),
                counts = it.counts,
                points = it.points,
                extraPoints = it.extraPoints,
                description1 = it.description1,
                description2 = it.description2
            )
        }
    }
}
