package edu.handong.cseemileage.mileage.mileageRecord.service

import edu.handong.cseemileage.mileage.mileageRecord.dto.MileageRecordDto
import edu.handong.cseemileage.mileage.mileageRecord.exception.MileageRecordNotFoundException
import edu.handong.cseemileage.mileage.mileageRecord.repository.MileageRecordRepository
import org.springframework.stereotype.Service

@Service
class MileageRecordQueryService(
    val mileageRecordRepository: MileageRecordRepository
) {
    fun getRecordById(id: Int): MileageRecordDto.Info {
        val found = mileageRecordRepository
            .findById(id)
            .orElseThrow(::MileageRecordNotFoundException)

        return MileageRecordDto.Info(
            semester = found.semesterItem.semesterName,
            category = found.semesterItem.category.name,
            subcategory = found.semesterItem.item.name,
            studentId = found.student.sid,
            studentName = found.student.name,
            points = found.semesterItem.pointValue,
            counts = found.counts,
            regDate = found.regDate.toString(),
            modDate = found.modDate.toString()
        )
    }

    fun getAll(): List<MileageRecordDto.Info> {
        return mileageRecordRepository
            .findAll()
            .map { MileageRecordDto.Info(it) }
    }
}
